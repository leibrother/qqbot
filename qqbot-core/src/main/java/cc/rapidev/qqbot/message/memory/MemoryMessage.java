package cc.rapidev.qqbot.message.memory;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author leibrother
 */
@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemoryMessage implements Serializable, Comparable<MemoryMessage> {

    public MemoryMessage(Builder builder) {
        this.id = builder.id;
        this.bot = builder.bot;
        this.text = builder.text;
        this.metadata = builder.metadata;
        this.timestamp = builder.timestamp;
    }

    private String id;
    private boolean bot;
    private String text;
    private Map<String, Object> metadata;
    private LocalDateTime timestamp;

    public <T> T getMetadata(String key, Class<T> clazz) {
        return getMetadata(key, clazz, null);
    }

    public <T> T getMetadata(String key, Class<T> clazz, T defaultValue) {
        if (metadata == null || !metadata.containsKey(key)) {
            return defaultValue;
        }
        Object value = metadata.get(key);
        if (clazz.isAssignableFrom(value.getClass())) {
            return clazz.cast(value);
        } else {
            throw new ClassCastException("the value of %s cannot be cast to %s".formatted(key, value.getClass()));
        }
    }

    @Override
    public int compareTo(MemoryMessage message) {
        return this.timestamp.compareTo(message.getTimestamp());
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id;
        private boolean bot;
        private String text;
        private LocalDateTime timestamp;
        private Map<String, Object> metadata = new HashMap<>();

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder bot(boolean bot) {
            this.bot = bot;
            return this;
        }

        public Builder text(String text) {
            this.text = text;
            return this;
        }

        public Builder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder metadata(Map<String, Object> metadata) {
            this.metadata = metadata;
            return this;
        }

        public Builder addMetadata(String key, Object value) {
            if (metadata == null) {
                metadata = new HashMap<>();
            }
            metadata.put(key, value);
            return this;
        }

        public MemoryMessage build() {
            return new MemoryMessage(this);
        }

    }

}
