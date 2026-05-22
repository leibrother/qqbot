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
            case MESSAGE_CREATE -> Topic.ofGuild(data.get("channel_id").textValue());
            case AT_MESSAGE_CREATE -> Topic.ofGuildAt(data.get("channel_id").textValue());
            case C2C_MESSAGE_CREATE -> Topic.ofPrivate(data.path("author").get("id").textValue());
            case DIRECT_MESSAGE_CREATE -> Topic.ofDirect(data.get("guild_id").textValue());
            case GROUP_AT_MESSAGE_CREATE -> Topic.ofGroupAt(data.get("group_id").textValue());
            default -> null;
        };
    }

}
