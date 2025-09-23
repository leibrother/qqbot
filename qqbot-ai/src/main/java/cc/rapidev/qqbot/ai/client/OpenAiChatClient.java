package cc.rapidev.qqbot.ai.client;

import cc.rapidev.qqbot.ai.AiConfig;
import cc.rapidev.qqbot.ai.tools.AiTool;
import cc.rapidev.qqbot.ai.tools.Today;
import cc.rapidev.qqbot.message.MessageContext;
import cc.rapidev.qqbot.message.memory.MemoryMessage;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.data.message.*;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.openai.OpenAiChatModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    /**
     * 获取工具的定义列表
     *
     * @return 工具定义列表
     */
    private List<ToolSpecification> getToolSpecifications() {
        return this.toolSpecifications.values().stream().toList();
    }

    /**
     * 执行工具
     *
     * @param context   上下文
     * @param name      工具名
     * @param arguments 参数
     * @return 执行结果
     */
    private String invokeTool(MessageContext context, String name, String arguments) {
        AiTool tool = this.tools.get(name);
        if (tool == null) {
            throw new IllegalArgumentException("no such tool: " + name);
        }
        return tool.invoke(context, arguments);
    }

    /**
     * 执行工具请求
     *
     * @param context 消息上下文
     * @param request 工具执行请求
     * @return 执行结果集合
     */
    private ToolExecutionResultMessage invokeTool(MessageContext context, ToolExecutionRequest request) {
        String name = request.name();
        String arguments = request.arguments();
        String result = invokeTool(context, name, arguments);
        return ToolExecutionResultMessage.from(request, result);
    }

    @Override
    public String chat(MessageContext context, List<MemoryMessage> messages) {
        List<ChatMessage> messageList = messages.stream()
                .map(msg -> msg.isBot() ? AiMessage.from(msg.getText()) : UserMessage.from(msg.getText()))
                .collect(Collectors.toList());
        if (this.prompt != null) {
            messageList.addFirst(this.prompt);
        }
        return completion(context, messageList);
    }

    /**
     * 对话补全
     *
     * @param context  消息上下文
     * @param messages 对话列表
     * @return 补全结果
     */
    private String completion(MessageContext context, List<ChatMessage> messages) {
        ChatRequest request = ChatRequest.builder()
                .messages(messages)
                .toolSpecifications(getToolSpecifications())
                .build();
        AiMessage response = chatModel.chat(request).aiMessage();
        // 工具执行
        if (response.hasToolExecutionRequests()) {
            List<ToolExecutionResultMessage> toolMessages = response.toolExecutionRequests().stream()
                    .map(req -> this.invokeTool(context, req)).toList();
            ArrayList<ChatMessage> newMessages = new ArrayList<>(messages);
            newMessages.add(response);
            newMessages.addAll(toolMessages);
            return completion(context, newMessages);
        }
        return response.text();
    }

}
