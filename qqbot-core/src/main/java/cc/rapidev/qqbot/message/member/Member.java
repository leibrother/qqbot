package cc.rapidev.qqbot.message.member;

import cc.rapidev.qqbot.common.Author;
import cc.rapidev.qqbot.common.Topic;

import java.util.Objects;

/**
 * @author leibrother
 */
public record Member(
        Topic topic,
        Author author
) {

    public String id() {
        return author.id();
    }

    public String openid() {
        return author.openid();
    }

    public String avatar() {
        return author.avatar();
    }

    public String username() {
        return author.username();
    }

    public boolean bot() {
        return author.bot();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return Objects.equals(id(), member.id())
                && Objects.equals(topic(), member.topic())
                && Objects.equals(openid(), member.openid())
                && Objects.equals(avatar(), member.avatar())
                && Objects.equals(username(), member.username())
                && Objects.equals(bot(), member.bot());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id(), topic(), openid(), avatar(), username(), bot());
    }

}
