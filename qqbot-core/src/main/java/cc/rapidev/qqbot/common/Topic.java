package cc.rapidev.qqbot.common;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

/**
 * 消息的来源（主题）
 * <p>有如下场景：</p>
 * <ul>
 *     <li>单聊消息，此时{@code event}为{@code C2C_MESSAGE_CREATE}，{@code id}为发送消息的用户ID</li>
 *     <li>群聊@消息，此时{@code event}为{@code GROUP_AT_MESSAGE_CREATE}，{@code id}为群ID</li>
 *     <li>频道私信消息，此时{@code event}为{@code DIRECT_MESSAGE_CREATE}，{@code id}为GuildID</li>
 *     <li>文字子频道@消息，此时{@code event}为{@code AT_MESSAGE_CREATE}，{@code id}子频道ID</li>
 *     <li>文字子频道全量消息，此时{@code event}为{@code MESSAGE_CREATE}，{@code id}子频道ID</li>
 * </ul>
 * <p>可以通过{@code id}向对应的来源回复消息</p>
 *
 * @author leibrother
 */
@Getter
@Setter(AccessLevel.PROTECTED)
public class Topic implements Serializable {

    private Events event;
    private String id;

    public Topic(Events event, String id) {
        this.event = event;
        this.id = id;
    }

    public boolean isPrivate() {
        return Events.C2C_MESSAGE_CREATE.equals(event);
    }

    public boolean isGroupAt() {
        return Events.GROUP_AT_MESSAGE_CREATE.equals(event);
    }

    public boolean isGuild() {
        return Events.MESSAGE_CREATE.equals(event);
    }

    public boolean isGuildAt() {
        return Events.AT_MESSAGE_CREATE.equals(event);
    }

    public boolean isDirect() {
        return Events.DIRECT_MESSAGE_CREATE.equals(event);
    }

    @Override
    public int hashCode() {
        return Objects.hash(event, id);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Topic topic)) return false;
        return event == topic.event && Objects.equals(id, topic.id);
    }

    @Override
    public String toString() {
        return event + ":" + id;
    }

    public static Topic ofPrivate(String id) {
        return new Topic(Events.C2C_MESSAGE_CREATE, id);
    }

    public static Topic ofGroupAt(String id) {
        return new Topic(Events.GROUP_AT_MESSAGE_CREATE, id);
    }

    public static Topic ofGuild(String id) {
        return new Topic(Events.MESSAGE_CREATE, id);
    }

    public static Topic ofGuildAt(String id) {
        return new Topic(Events.AT_MESSAGE_CREATE, id);
    }

    public static Topic ofDirect(String id) {
        return new Topic(Events.DIRECT_MESSAGE_CREATE, id);
    }

}
