package cc.rapidev.qqbot.message.member;

import cc.rapidev.qqbot.common.Author;
import cc.rapidev.qqbot.common.Topic;
import cc.rapidev.qqbot.database.entity.annotations.DBTable;
import cc.rapidev.qqbot.database.entity.annotations.TBColumn;
import cc.rapidev.qqbot.database.entity.annotations.TBPrimaryKey;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

/**
 * @author leibrother
 */
@Getter
@Setter
@NoArgsConstructor
@DBTable(name = "bot_members")
public class MemberEntity {

    @TBColumn
    @TBPrimaryKey
    private String id;
    @TBColumn
    @TBPrimaryKey
    private String topic;
    @TBColumn
    private String openid;
    @TBColumn
    private String avatar;
    @TBColumn
    private String username;
    @TBColumn
    private boolean bot;

    @Override
    public int hashCode() {
        return Objects.hash(id, topic, openid, avatar, username);
    }

    public Member toRecord() {
        Topic topic = Topic.resolve(this.topic);
        Author author = new Author(this.id, this.openid, this.avatar, this.username, this.bot);
        return new Member(topic, author);
    }

    public static MemberEntity of(Member member) {
        MemberEntity entity = new MemberEntity();
        entity.id = member.id();
        entity.topic = member.topic().code();
        entity.openid = member.openid();
        entity.avatar = member.avatar();
        entity.username = member.username();
        entity.bot = member.bot();
        return entity;
    }

}
