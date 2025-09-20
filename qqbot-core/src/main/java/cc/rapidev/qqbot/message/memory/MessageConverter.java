package cc.rapidev.qqbot.message.memory;

import cc.rapidev.qqbot.BotPayload;
import cc.rapidev.qqbot.common.Constant;
import cc.rapidev.qqbot.common.interfaces.Converter;
import cc.rapidev.qqbot.message.MessageContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author leibrother
 */
public class MessageConverter implements Converter<MessageContext, MemoryMessage> {

    @Override
    public MemoryMessage convert(MessageContext context) {
        BotPayload payload = context.getPayload();
        JsonNode data = payload.getData();
        // id
        String id = data.get("id").asText();
        // text
        String content = data.get("content").asText();
        // timestamp
        String timestamp = data.get("timestamp").asText();
        LocalDateTime datetime = OffsetDateTime.parse(timestamp, Constant.dateTimeFormatter).toLocalDateTime();
        // metadata
        Map<String, Object> metadata = getMetadata(context);
        return new MemoryMessage(id, content, datetime, metadata);
    }

    private Map<String, Object> getMetadata(MessageContext context) {
        return new HashMap<>();
    }

}
