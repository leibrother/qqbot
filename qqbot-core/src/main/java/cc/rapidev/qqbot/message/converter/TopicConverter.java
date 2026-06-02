package cc.rapidev.qqbot.message.converter;

import cc.rapidev.qqbot.BotPayload;
import cc.rapidev.qqbot.common.Events;
import cc.rapidev.qqbot.common.Topic;
import cc.rapidev.qqbot.common.interfaces.Converter;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author leibrother
 */
public class TopicConverter implements Converter<BotPayload, Topic> {

    public static final TopicConverter INSTANCE = new TopicConverter();

    @Override
    public Topic convert(BotPayload payload) {
        Events event = Events.valueOf(payload.event());
        JsonNode data = payload.data();
        return switch (event) {
            // 单聊
            case C2C_MESSAGE_CREATE -> Topic.ofPrivate(data.path("author").get("id").textValue());
            // 群聊AT
            case GROUP_AT_MESSAGE_CREATE -> Topic.ofGroup(data.get("group_id").textValue());
            // 频道全量 , 频道AT
            case MESSAGE_CREATE, AT_MESSAGE_CREATE -> Topic.ofGuild(data.get("channel_id").textValue());
            // 频道单聊
            case DIRECT_MESSAGE_CREATE -> Topic.ofDirect(data.get("guild_id").textValue());
            // 按钮回调
            case INTERACTION_CREATE -> switch (data.get("chat_type").textValue()) {
                // 频道
                case "0" -> {
                    if (data.has("guild_id")) {
                        // 频道
                        yield Topic.ofGuild(data.get("guild_id").textValue());
                    } else if (data.has("channel_id")) {
                        // 文字子频道
                        yield Topic.ofGuild(data.get("channel_id").textValue());
                    }
                    yield null;
                }
                // 群聊
                case "1" -> Topic.ofGroup(data.get("group_openid").textValue());
                // 单聊
                case "2" -> Topic.ofPrivate(data.get("user_openid").textValue());
                default -> null;
            };
            default -> null;
        };
    }

}
