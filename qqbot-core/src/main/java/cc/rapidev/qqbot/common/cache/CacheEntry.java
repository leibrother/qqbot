package cc.rapidev.qqbot.common.cache;


import java.util.concurrent.TimeUnit;

public record CacheEntry(Object value, long expireAt) {

    public boolean isExpired() {
        return this.expireAt > 0 && expireAt <= System.currentTimeMillis();
    }

    public static CacheEntry create(Object value, long ttl, TimeUnit unit) {
        long expireAt = 0;
        if (ttl > 0) {
            long millis = unit.toMillis(ttl);
            expireAt = System.currentTimeMillis() + millis;
        }
        return new CacheEntry(value, expireAt);
    }

}