package cc.rapidev.qqbot.api.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author leibrother
 */
@Getter
@Setter
@ToString
public class AccessTokenResponse implements Serializable {

    private String accessToken;

    private Integer expiresIn;

    private LocalDateTime expiresTime;

    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
        if (expiresIn != null) {
            this.expiresTime = LocalDateTime.now().plusSeconds(expiresIn - 30);
        } else {
            this.expiresTime = LocalDateTime.now();
        }
    }

    public boolean isExpire() {
        return LocalDateTime.now().isAfter(this.expiresTime);
    }

}
