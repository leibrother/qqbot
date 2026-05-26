package cc.rapidev.qqbot.message.model;

import java.util.Objects;

/**
 * @author leibrother
 */
public record Author(
        String openid,
        String avatar,
        String username,
        boolean bot
) {

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        Author author = (Author) object;
        return Objects.equals(openid, author.openid);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(openid);
    }

}
