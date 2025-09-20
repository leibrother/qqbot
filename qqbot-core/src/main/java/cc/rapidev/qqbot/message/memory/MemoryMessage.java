package cc.rapidev.qqbot.message.memory;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author leibrother
 */
@Getter
@Setter
public class MemoryMessage implements Serializable {

    public MemoryMessage(String id, String text) {
        this.id = id;
        this.text = text;
        this.metadata = new HashMap<>();
    }

    public MemoryMessage(String id, String text, Map<String, Object> metadata) {
        this.id = id;
        this.text = text;
        this.metadata = metadata;
    }

    private String id;
    private Map<String, Object> metadata;
    private String text;

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

}
