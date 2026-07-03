package cc.rapidev.qqbot.common.cache;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Cacheable {

    /**
     * 缓存key（支持 EL）
     */
    String key();

    /**
     * 生存时间（单位：秒），小于等于0为无限制
     */
    long ttl() default 0;

}