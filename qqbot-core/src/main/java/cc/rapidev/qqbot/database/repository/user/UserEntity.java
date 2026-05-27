package cc.rapidev.qqbot.database.repository.user;

import cc.rapidev.qqbot.database.entity.annotations.DBTable;
import cc.rapidev.qqbot.database.entity.annotations.TBColumn;
import cc.rapidev.qqbot.database.entity.annotations.TBPrimaryKey;
import cc.rapidev.qqbot.message.model.Author;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author leibrother
 */
@Getter
@Setter
@NoArgsConstructor
@DBTable(name = "bot_users")
public class UserEntity {

    @TBColumn
    @TBPrimaryKey
    private String openid;
    @TBColumn
    private String avatar;
    @TBColumn
    private String username;
    @TBColumn
    private boolean bot;

    public Author toAuthor() {
        return new Author(
                openid,
                avatar,
                username,
                bot
        );
    }

    public static UserEntity from(Author author) {
        UserEntity entity = new UserEntity();
        entity.setOpenid(author.openid());
        entity.setAvatar(author.avatar());
        entity.setUsername(author.username());
        entity.setBot(author.bot());
        return entity;
    }

}
