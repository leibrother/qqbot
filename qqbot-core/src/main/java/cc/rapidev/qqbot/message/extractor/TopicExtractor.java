package cc.rapidev.qqbot.message.extractor;

import cc.rapidev.qqbot.common.Topic;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author leibrother
 */
public class TopicExtractor extends Extractor<Topic> {

    private static final TopicExtractor INSTANCE = new TopicExtractor();

    public static TopicExtractor instance() {
        return INSTANCE;
    }

    @Override
    protected Topic byC2CMessageCreate(JsonNode data) {
        return Topic.ofPrivate(data.path("author").get("id").textValue());
    }

    @Override
    protected Topic byGroupAtMessageCreate(JsonNode data) {
        return Topic.ofGroup(data.get("group_id").textValue());
    }

    @Override
    protected Topic byMessageCreate(JsonNode data) {
        return Topic.ofGuild(data.get("channel_id").textValue());
    }

    @Override
    protected Topic byAtMessageCreate(JsonNode data) {
        return byMessageCreate(data);
    }

    @Override
    protected Topic byDirectMessageCreate(JsonNode data) {
        return Topic.ofDirect(data.get("guild_id").textValue());
    }

    @Override
    protected Topic byInteractionCreate(JsonNode data) {
        String type = data.get("chat_type").textValue();
        return switch (type) {
            case "0" -> {
                if (data.has("guild_id")) {
                    yield Topic.ofGuild(data.get("guild_id").textValue());
                } else {
                    yield Topic.ofGuild(data.get("channel_id").textValue());
                }
            }
            case "1" -> Topic.ofGroup(data.get("group_openid").textValue());
            case "2" -> Topic.ofPrivate(data.get("user_openid").textValue());
            default -> throw new IllegalArgumentException("unknown chat_type: " + type);
        };
    }

}
