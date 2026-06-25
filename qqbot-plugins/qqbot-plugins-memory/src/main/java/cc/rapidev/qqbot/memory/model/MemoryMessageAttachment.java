package cc.rapidev.qqbot.memory.model;

import cc.rapidev.qqbot.common.utils.Asserts;
import cc.rapidev.qqbot.message.model.MessageAttachmentGeneric;

/**
 * @author leibrother
 */
public record MemoryMessageAttachment(
        String id,
        String filename,
        String type,
        String url
) {

    public MemoryMessageAttachment(Builder builder) {
        Asserts.notempty(builder.id, "");
        this(builder.id, builder.filename, builder.type, builder.url);
    }

    public static MemoryMessageAttachment of(MessageAttachmentGeneric generic) {
        Builder builder;
        if (generic.isImage()) {
            builder = builder().image();
        } else if (generic.isVideo()) {
            builder = builder().video();
        } else if (generic.isAudio()) {
            builder = builder().audio();
        } else {
            builder = builder();
        }
        return builder.id(generic.id())
                .filename(generic.filename())
                .url(generic.url())
                .build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id;
        private String filename;
        private String type = "other";
        private String url;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder filename(String filename) {
            this.filename = filename;
            return this;
        }

        public Builder image() {
            this.type = "image";
            return this;
        }

        public Builder video() {
            this.type = "video";
            return this;
        }

        public Builder audio() {
            this.type = "audio";
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public MemoryMessageAttachment build() {
            return new MemoryMessageAttachment(this);
        }

    }

}
