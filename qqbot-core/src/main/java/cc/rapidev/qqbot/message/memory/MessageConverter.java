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
        String id = data.get("id").asText();
        String content = data.get("content").asText();
        Map<String, Object> metadata = getMetadata(context);
        return new MemoryMessage(id, content, metadata);
    }

    private Map<String, Object> getMetadata(MessageContext context) {
        Map<String, Object> metadata = new HashMap<>();
        BotPayload payload = context.getPayload();
        JsonNode data = payload.getData();
        // id
        metadata.put("id", data.get("id").asText());
        // timestamp
        String timestamp = data.get("timestamp").asText();
        LocalDateTime datetime = OffsetDateTime.parse(timestamp, Constant.dateTimeFormatter).toLocalDateTime();
        metadata.put("timestamp", datetime);
        return metadata;
    }

}
