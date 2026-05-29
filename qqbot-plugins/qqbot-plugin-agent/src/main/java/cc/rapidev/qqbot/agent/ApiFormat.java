package cc.rapidev.qqbot.agent;

import dev.langchain4j.model.anthropic.AnthropicChatModel;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;

/**
 * @author leibrother
 */
public enum ApiFormat {

    OpenAI(OpenAiChatModel.class),
    Anthropic(AnthropicChatModel.class);

    private final Class<? extends ChatModel> clazz;

    ApiFormat(Class<? extends ChatModel> clazz) {
        this.clazz = clazz;
    }

    public Class<? extends ChatModel> clazz() {
        return this.clazz;
    }

}
