package cc.rapidev.qqbot.common.cache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author leibrother
 */
public class CacheManager {

    private static final CacheManager INSTANCE = new CacheManager(1024);

    private final int max;
    private final ConcurrentHashMap<String, CacheEntry> cache;

    private CacheManager(int max) {
        this.max = max;
        this.cache = new ConcurrentHashMap<>(max);
    }

    public static CacheManager getInstance() {
        return INSTANCE;
    }

    public Object get(String key) {
        CacheEntry entry = cache.get(key);
        if (entry == null) {
            return null;
        }
        if (entry.isExpired()) {
            cache.remove(key);
            return null;
        }
        return entry.value();
    }

    public void set(String key, Object value, long ttl) {
        if (cache.size() >= max) {
            evictFirst();
        }
        cache.put(key, CacheEntry.create(value, ttl, TimeUnit.SECONDS));
    }

    public void remove(String key) {
        cache.remove(key);
    }

    private void evictFirst() {
        String eldestKey = cache.keySet().iterator().next();
        cache.remove(eldestKey);
    }

}
