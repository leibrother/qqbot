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
            // 频道全量
            case MESSAGE_CREATE -> Topic.ofGuild(data.get("channel_id").textValue());
            // 频道AT
            case AT_MESSAGE_CREATE -> Topic.ofGuildAt(data.get("channel_id").textValue());
            // 单聊
            case C2C_MESSAGE_CREATE -> Topic.ofPrivate(data.path("author").get("id").textValue());
            // 频道单聊
            case DIRECT_MESSAGE_CREATE -> Topic.ofDirect(data.get("guild_id").textValue());
            // 群聊AT
            case GROUP_AT_MESSAGE_CREATE -> Topic.ofGroupAt(data.get("group_id").textValue());
            // 按钮回调
            case INTERACTION_CREATE -> {
                String type = data.get("chat_type").textValue();
                if ("0".equals(type)) {
                    // 频道
                    if (data.has("guild_id")) {
                        // 频道
                        yield Topic.ofGuildAt(data.get("guild_id").textValue());
                    } else if (data.has("channel_id")) {
                        // 文字子频道
                        yield Topic.ofGuildAt(data.get("channel_id").textValue());
                    }
                } else if ("1".equals(type)) {
                    // 群聊
                    yield Topic.ofGroupAt(data.get("group_openid").textValue());
                } else if ("2".equals(type)) {
                    // 单聊
                    yield Topic.ofPrivate(data.get("user_openid").textValue());
                }
                yield null;
            }
            default -> null;
        };
    }

}
