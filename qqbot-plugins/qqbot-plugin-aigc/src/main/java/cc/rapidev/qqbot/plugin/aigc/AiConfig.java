package cc.rapidev.qqbot.plugin.aigc;

import cc.rapidev.qqbot.common.Config;
import cc.rapidev.qqbot.common.utils.ObjectUtils;
import cc.rapidev.qqbot.plugin.aigc.client.OpenAiChatClient;
import cc.rapidev.qqbot.plugin.aigc.client.OpenAiImageClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author leibrother
 */
public class AiConfig {

    // chat
    public static final String PROPERTY_CHAT_ENABLE = "bot.message.ai.chat.enable";
    public static final String PROPERTY_CHAT_CLIENT = "bot.message.ai.chat.client";
    public static final String PROPERTY_CHAT_BASEURL = "bot.message.ai.chat.baseUrl";
    public static final String PROPERTY_CHAT_APIKEY = "bot.message.ai.chat.apiKey";
    public static final String PROPERTY_CHAT_MODEL = "bot.message.ai.chat.model";
    public static final String PROPERTY_CHAT_PROMPT = "bot.message.ai.chat.prompt";

    // image
    public static final String PROPERTY_IMAGE_ENABLE = "bot.message.ai.image.enable";
    public static final String PROPERTY_IMAGE_CLIENT = "bot.message.ai.image.client";
    public static final String PROPERTY_IMAGE_BASEURL = "bot.message.ai.image.baseUrl";
    public static final String PROPERTY_IMAGE_APIKEY = "bot.message.ai.image.apiKey";
    public static final String PROPERTY_IMAGE_MODEL = "bot.message.ai.image.model";
    public static final String PROPERTY_IMAGE_KEYWORDS = "bot.message.ai.image.keywords";

    private final Config config;

    public AiConfig(Config config) {
        this.config = config;
    }

    public boolean isChatEnable() {
        return config.getPropertyAsBoolean(PROPERTY_CHAT_ENABLE, false);
    }

    public String getChatClient() {
        return config.getProperty(PROPERTY_CHAT_CLIENT, OpenAiChatClient.class.getName());
    }

    public String getChatBaseUrl() {
        String property = config.getProperty(PROPERTY_CHAT_BASEURL);
        ObjectUtils._assert(property, "please set %s".formatted(PROPERTY_CHAT_BASEURL));
        return property;
    }

    public String getChatApiKey() {
        String property = config.getProperty(PROPERTY_CHAT_APIKEY);
        ObjectUtils._assert(property, "please set %s".formatted(PROPERTY_CHAT_APIKEY));
        return property;
    }

    public String getChatModel() {
        String property = config.getProperty(PROPERTY_CHAT_MODEL);
        ObjectUtils._assert(property, "please set %s".formatted(PROPERTY_CHAT_MODEL));
        return property;
    }

    public String getChatPrompt() {
        return config.getProperty(PROPERTY_CHAT_PROMPT);
    }

    public boolean isImageEnable() {
        return config.getPropertyAsBoolean(PROPERTY_IMAGE_ENABLE, false);
    }

    public String getImageClient() {
        return config.getProperty(PROPERTY_IMAGE_CLIENT, OpenAiImageClient.class.getName());
    }

    public String getImageBaseUrl() {
        String property = config.getProperty(PROPERTY_IMAGE_BASEURL);
        ObjectUtils._assert(property, "please set %s".formatted(PROPERTY_IMAGE_BASEURL));
        return property;
    }

    public String getImageApiKey() {
        String property = config.getProperty(PROPERTY_IMAGE_APIKEY);
        ObjectUtils._assert(property, "please set %s".formatted(PROPERTY_IMAGE_APIKEY));
        return property;
    }

    public String getImageModel() {
        String property = config.getProperty(PROPERTY_IMAGE_MODEL);
        ObjectUtils._assert(property, "please set %s".formatted(PROPERTY_IMAGE_MODEL));
        return property;
    }

    public List<String> getImageKeywords() {
        String property = config.getProperty(PROPERTY_IMAGE_KEYWORDS);
        if (property != null) {
            return Arrays.asList(property.trim().split(","));
        }
        return new ArrayList<>();
    }

}
