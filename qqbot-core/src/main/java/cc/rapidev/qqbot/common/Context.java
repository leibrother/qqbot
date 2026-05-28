package cc.rapidev.qqbot.common;

import java.util.HashMap;
import java.util.Map;

/**
 * @author leibrother
 */
public final class Context {

    private final static Context empty = new Context();

    private final Context prev;
    private final Map<String, Object> map = new HashMap<>();

    public Context() {
        this(null);
    }

    public Context(Context prev) {
        this.prev = prev;
    }

    public Map<String, Object> map() {
        Map<String, Object> result = new HashMap<>();
        if (this.prev != null) {
            result.putAll(this.prev.map());
        }
        result.putAll(this.map);
        return result;
    }

    public void set(String key, Object value) {
        map.put(key, value);
    }

    public Object get(String key) {
        if (this.prev != null) {
            return this.prev.get(key);
        }
        return map.get(key);
    }

    public Object get(String key, Object defaultValue) {
        if (this.prev != null) {
            return this.prev.get(key, defaultValue);
        }
        return map.getOrDefault(key, defaultValue);
    }

    public Context with(String key, Object value) {
        Context context = new Context(this);
        context.set(key, value);
        return context;
    }

    public static Context empty() {
        return empty;
    }

}
