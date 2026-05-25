package cc.rapidev.qqbot.memory.model;

import cc.rapidev.qqbot.api.model.Message;
import cc.rapidev.qqbot.api.response.MessageMediaResponse;
import cc.rapidev.qqbot.api.response.MessageResponse;
import cc.rapidev.qqbot.message.model.MessageAttachmentGeneric;
import cc.rapidev.qqbot.message.model.MessageGeneric;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author leibrother
 */
public record MemoryMessage(
        String id,
        String content,
        List<MessageAttachmentGeneric> attachments,
        LocalDateTime timestamp,
        Boolean bot
) {

    public static MemoryMessage of(MessageGeneric generic) {
        return new MemoryMessage(
                generic.id(),
                generic.content(),
                Collections.unmodifiableList(generic.attachments()),
                generic.timestamp(),
                false
        );
    }

    public static MemoryMessage of(Message message, MessageResponse response) {
        MessageAttachmentGeneric attachment = null;
        if (message.isMedia()) {
            MessageMediaResponse media = message.getMedia();
            String filename = "";
            Matcher matcher = Pattern.compile(".*/([^?]+)(?:\\?.*)?").matcher(media.getFileLink());
            if (matcher.find()) {
                filename = matcher.group(1).trim();
            }
            String type = switch (media.getFileType()) {
                case 1 -> "image";
                case 2 -> "video";
                case 3 -> "audio";
                default -> "unknown";
            };
            attachment = new MessageAttachmentGeneric(
                    media.getId() == null || media.getId().isEmpty() ? media.getFileInfo() : media.getId(),
                    filename,
                    type,
                    media.getFileLink(),
                    0,
                    0,
                    0,
                    null,
                    null
            );
        }

        String content;
        if (message.isText()) {
            content = message.getContent();
        } else if (message.isMarkdown()) {
            content = message.getMarkdown().getContent();
        } else {
            content = "";
        }

        return new MemoryMessage(
                response.getId(),
                content,
                attachment == null ? List.of() : List.of(attachment),
                response.getTime(),
                true
        );
    }

}
