package cc.rapidev.qqbot.agent.model;

import io.agentscope.core.model.AnthropicChatModel;
import io.agentscope.core.model.Model;
import io.agentscope.core.model.OpenAIChatModel;

/**
 * @author leibrother
 */
public class ModelFactory {

    public static Model produce(ModelValues config) {
        return switch (config.apiFormat()) {
            case OpenAI -> OpenAIChatModel.builder()
                    .baseUrl(config.baseUrl())
                    .apiKey(config.apiKey())
                    .modelName(config.modelName())
                    .build();
            case Anthropic -> AnthropicChatModel.builder()
                    .baseUrl(config.baseUrl())
                    .apiKey(config.apiKey())
                    .modelName(config.modelName())
                    .build();
        };
    }

}
