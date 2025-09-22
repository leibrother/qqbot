package cc.rapidev.qqbot.ai.client;

import cc.rapidev.qqbot.ai.AiConfig;
import cc.rapidev.qqbot.ai.tools.AiTool;
import cc.rapidev.qqbot.ai.tools.Today;
import cc.rapidev.qqbot.message.memory.MemoryMessage;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.data.message.*;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.openai.OpenAiChatModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author leibrother
 */
public class OpenAiChatClient implements AiChatClient {

    private final AiConfig config;
    private final OpenAiChatModel chatModel;
    private final Map<String, AiTool> tools = new HashMap<>();
    private final Map<String, ToolSpecification> toolSpecifications = new HashMap<>();
    private SystemMessage prompt;

    public OpenAiChatClient(AiConfig config) {
        this.config = config;
        this.chatModel = OpenAiChatModel.builder()
                .baseUrl(config.getChatBaseUrl())
                .apiKey(config.getChatApiKey())
                .modelName(config.getChatModel())
                .strictTools(true)
                .build();
        this.init();
    }

    private void init() {
        String prompt = config.getChatPrompt();
        if (prompt != null && !prompt.isEmpty()) {
            this.prompt = SystemMessage.from(prompt);
        }
        this.addTool(new Today());
    }

    @Override
    public void addTool(AiTool tool) {
        ToolSpecification specification = tool.specification();
        String name = specification.name();
        if (this.tools.containsKey(name)) {
            throw new IllegalArgumentException("tool already exists: " + name);
        }
        this.tools.put(name, tool);
        this.toolSpecifications.put(name, specification);
    }

    private List<ToolSpecification> getToolSpecifications() {
        return this.toolSpecifications.values().stream().toList();
    }

    private String invokeTool(String name, String arguments) {
        AiTool tool = this.tools.get(name);
        if (tool == null) {
            throw new IllegalArgumentException("no such tool: " + name);
        }
        return tool.invoke(arguments);
    }

    @Override
    public String chat(List<MemoryMessage> messages) {
        List<ChatMessage> chatMessages = generateChatMessages(messages);
        return doChat(chatMessages);
    }

    private String doChat(List<ChatMessage> messages) {
        ChatRequest request = ChatRequest.builder()
                .messages(messages)
                .toolSpecifications(getToolSpecifications())
                .build();
        AiMessage response = chatModel.chat(request).aiMessage();
        if (response.hasToolExecutionRequests()) {
            List<ToolExecutionResultMessage> toolMessages = new ArrayList<>();
            response.toolExecutionRequests().forEach(toolExecutionRequest -> {
                String name = toolExecutionRequest.name();
                String arguments = toolExecutionRequest.arguments();
                String result = invokeTool(name, arguments);
                toolMessages.add(ToolExecutionResultMessage.from(toolExecutionRequest, result));
            });
            ArrayList<ChatMessage> chatMessages = new ArrayList<>(messages);
            chatMessages.add(response);
            chatMessages.addAll(toolMessages);
            return doChat(chatMessages);
        }
        return response.text();
    }

    private List<ChatMessage> generateChatMessages(List<MemoryMessage> messages) {
        List<ChatMessage> chatMessages = new ArrayList<>();
        if (this.prompt != null) {
            chatMessages.add(this.prompt);
        }
        for (MemoryMessage message : messages) {
            if (message.isBot()) {
                chatMessages.add(AiMessage.from(message.getText()));
            } else {
                chatMessages.add(UserMessage.from(message.getText()));
            }
        }
        return chatMessages;
    }

}
