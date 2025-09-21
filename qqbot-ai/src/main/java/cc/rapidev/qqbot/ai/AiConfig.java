package cc.rapidev.qqbot.ai;

import cc.rapidev.qqbot.ai.client.OpenAiChatClient;
import cc.rapidev.qqbot.common.Config;
import cc.rapidev.qqbot.common.utils.ObjectUtils;

/**
 * @author leibrother
 */
public class AiConfig {

    public static final String PROPERTY_CLIENT = "bot.message.ai.client";
    public static final String PROPERTY_ENABLE = "bot.message.ai.enable";
    public static final String PROPERTY_BASEURL = "bot.message.ai.baseUrl";
    public static final String PROPERTY_APIKEY = "bot.message.ai.apiKey";
    public static final String PROPERTY_MODEL = "bot.message.ai.model";
    public static final String PROPERTY_PROMPT = "bot.message.ai.prompt";

    private final Config config;

    public AiConfig(Config config) {
        this.config = config;
    }

    public boolean isEnable() {
        return config.getPropertyAsBoolean(PROPERTY_ENABLE, false);
    }

    public String getClient() {
        return config.getProperty(PROPERTY_CLIENT, OpenAiChatClient.class.getName());
    }

    public String getBaseUrl() {
        String property = config.getProperty(PROPERTY_BASEURL);
        ObjectUtils._assert(property, "please set %s".formatted(PROPERTY_BASEURL));
        return property;
    }

    public String getApiKey() {
        String property = config.getProperty(PROPERTY_APIKEY);
        ObjectUtils._assert(property, "please set %s".formatted(PROPERTY_APIKEY));
        return property;
    }

    public String getModel() {
        String property = config.getProperty(PROPERTY_MODEL);
        ObjectUtils._assert(property, "please set %s".formatted(PROPERTY_MODEL));
        return property;
    }

    public String getPrompt() {
        return config.getProperty(PROPERTY_PROMPT);
    }

}
