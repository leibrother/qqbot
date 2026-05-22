package cc.rapidev.qqbot.message.model;

import cc.rapidev.qqbot.common.Constant;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author leibrother
 */
public record MessageGeneric(
        String id,
        String content,
        List<MessageAttachmentGeneric> attachments,
        LocalDateTime timestamp
) {

    public MessageGeneric(String id, String content, List<MessageAttachmentGeneric> attachments, String timestamp) {
        LocalDateTime datetime = null;
        if (timestamp != null && !timestamp.isEmpty()) {
            datetime = LocalDateTime.parse(timestamp, Constant.dateTimeFormatter);
        }
        this(id, content, attachments, datetime);
    }

}
