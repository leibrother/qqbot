package cc.rapidev.qqbot.memory.model;

import cc.rapidev.qqbot.api.model.Message;
import cc.rapidev.qqbot.api.response.MessageMediaResponse;
import cc.rapidev.qqbot.api.response.MessageResponse;
import cc.rapidev.qqbot.common.utils.Asserts;
import cc.rapidev.qqbot.message.model.MessageGeneric;

import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author leibrother
 */
public record MemoryMessage(
        String id,
        String content,
        List<MemoryMessageAttachment> attachments,
        LocalDateTime timestamp,
        boolean bot,
        Map<String, Object> metadata
) {

    public MemoryMessage(Builder builder) {
        Asserts.notempty(builder.id, "");
        this(
                builder.id,
                builder.content,
                builder.attachments,
                Optional.of(builder.timestamp).orElseGet(LocalDateTime::now),
                builder.bot,
                builder.metadata
        );
    }

    public MemoryMessage metadata(String key, Object value) {
        metadata.put(key, value);
        return this;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id;
        private String content;
        private List<MemoryMessageAttachment> attachments;
        private LocalDateTime timestamp;
        private boolean bot;
        private final Map<String, Object> metadata = new HashMap<>();

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder attachments(List<MemoryMessageAttachment> attachments) {
            this.attachments = attachments;
            return this;
        }

        public Builder addAttachment(MemoryMessageAttachment attachment) {
            if (this.attachments == null) {
                this.attachments = new ArrayList<>();
            }
            this.attachments.add(attachment);
            return this;
        }

        public Builder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder bot() {
            this.bot = true;
            return this;
        }

        public Builder metadata(String key, Object value) {
            this.metadata.put(key, value);
            return this;
        }

        public MemoryMessage build() {
            return new MemoryMessage(
                    id,
                    content,
                    attachments,
                    timestamp == null ? LocalDateTime.now() : timestamp,
                    bot,
                    metadata
            );
        }

    }

    public static MemoryMessage of(MessageGeneric generic) {
        return MemoryMessage.builder()
                .id(generic.id())
                .content(generic.content())
                .attachments(generic.attachments().stream().map(MemoryMessageAttachment::of).toList())
                .timestamp(generic.timestamp())
                .build();
    }

    public static MemoryMessage of(Message message, MessageResponse response) {
        Builder builder = MemoryMessage.builder().bot();
        builder.id(response.getId());
        builder.timestamp(response.getTime());
        if (message.isText()) {
            builder.content(message.getContent());
        } else if (message.isMarkdown()) {
            builder.content(message.getMarkdown().getContent());
        }
        if (message.isMedia()) {
            MessageMediaResponse media = message.getMedia();
            MemoryMessageAttachment.Builder attachment = MemoryMessageAttachment.builder();
            Matcher matcher = Pattern.compile(".*/([^?]+)(?:\\?.*)?").matcher(media.getFileLink());
            if (matcher.find()) {
                attachment.filename(matcher.group(1).trim());
            }
            if (media.isImage()) {
                attachment.image();
            } else if (media.isVideo()) {
                attachment.video();
            } else if (media.isAudio()) {
                attachment.audio();
            }
            attachment.url(media.getFileLink());
            builder.addAttachment(attachment.build());
        }
        return builder.build();
    }

}
