package cc.rapidev.qqbot.api.response;

import cc.rapidev.qqbot.common.Constant;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

/**
 * @author leibrother
 */
@Getter
@Setter(AccessLevel.PROTECTED)
@ToString
public class MessageResponse implements Serializable {

    private String id;

    private String timestamp;

    public LocalDateTime getTime() {
        OffsetDateTime offsetDateTime = OffsetDateTime.parse(timestamp, Constant.dateTimeFormatter);
        return offsetDateTime.toLocalDateTime();
    }

}
