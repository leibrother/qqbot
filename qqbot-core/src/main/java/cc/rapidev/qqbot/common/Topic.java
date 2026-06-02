package cc.rapidev.qqbot.common;

import org.jspecify.annotations.NonNull;

import java.io.Serializable;
import java.util.Objects;

/**
 * 消息的来源（主题）
 * <p>有如下场景：</p>
 * <ul>
 *     <li>单聊消息</li>
 *     <li>群聊消息</li>
 *     <li>频道私信消息</li>
 *     <li>文字子频道全量，包括AT消息</li>
 * </ul>
 * <p>可以通过{@code id}向对应的来源回复消息</p>
 *
 * @author leibrother
 */
public class Topic implements Serializable {

    public enum Type {
        PRIVATE,
        GROUP,
        GUILD,
        DIRECT
    }

    private final Type type;
    private final String id;

    private Topic(Type type, String id) {
        this.type = type;
        this.id = id;
    }

    public Type type() {
        return type;
    }

    public String id() {
        return id;
    }

    public boolean isPrivate() {
        return Type.PRIVATE.equals(type);
    }

    public boolean isGroup() {
        return Type.GROUP.equals(type);
    }

    public boolean isGuild() {
        return Type.GUILD.equals(type);
    }

    public boolean isDirect() {
        return Type.DIRECT.equals(type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, id);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Topic topic)) return false;
        return type == topic.type && Objects.equals(id, topic.id);
    }

    public @NonNull String code() {
        return type + ":" + id;
    }

    @Override
    public @NonNull String toString() {
        return code();
    }

    public static Topic ofPrivate(String id) {
        return new Topic(Type.PRIVATE, id);
    }

    public static Topic ofGroup(String id) {
        return new Topic(Type.GROUP, id);
    }

    public static Topic ofGuild(String id) {
        return new Topic(Type.GUILD, id);
    }

    public static Topic ofDirect(String id) {
        return new Topic(Type.DIRECT, id);
    }

}
