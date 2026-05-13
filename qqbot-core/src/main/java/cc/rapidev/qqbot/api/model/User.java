package cc.rapidev.qqbot.api.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * 用户对象
 *
 * @author leibrother
 * @see <a href="https://bot.q.qq.com/wiki/develop/api/openapi/user/model.html">用户对象</a>
 */
@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class User implements Serializable {

    private String id;

    private String username;

    private String avatar;

    private String unionOpenid;

    private String unionUserAccount;

    private boolean bot;

    public String getCleanUsername() {
        if (this.username.endsWith("-测试中")) {
            return this.username.substring(0, this.username.length() - 4);
        }
        return getUsername();
    }

}
