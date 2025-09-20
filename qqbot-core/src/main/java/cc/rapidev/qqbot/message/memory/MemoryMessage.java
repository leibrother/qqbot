package cc.rapidev.qqbot.message.memory;

import lombok.AccessLevel;
import lombok.Getter;
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
public class MemoryMessage implements Serializable, Comparable<MemoryMessage> {

    public MemoryMessage(String id, String text) {
        this(id, text, LocalDateTime.now(), new HashMap<>());
    }

    public MemoryMessage(String id, String text, LocalDateTime timestamp) {
        this(id, text, timestamp, new HashMap<>());
    }

    public MemoryMessage(String id, String text, LocalDateTime timestamp, Map<String, Object> metadata) {
        this.id = id;
        this.text = text;
        this.metadata = metadata;
        this.timestamp = timestamp;
    }

    private String id;
    private Map<String, Object> metadata;
    private String text;
    private LocalDateTime timestamp;

    public void addMetadata(String key, Object value) {
        if (metadata == null) {
            metadata = new HashMap<>();
        }
        metadata.put(key, value);
    }

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

}
