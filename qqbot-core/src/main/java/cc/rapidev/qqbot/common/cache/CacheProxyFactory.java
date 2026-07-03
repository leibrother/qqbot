package cc.rapidev.qqbot.common.cache;

import java.lang.reflect.Proxy;

public class CacheProxyFactory {

    @SuppressWarnings("unchecked")
    public static <T> T create(T target) {
        return (T) Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                new CacheInvocationHandler(target)
        );
    }

}