package cc.rapidev.qqbot.api.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author leibrother
 */
@Getter
@Setter
@ToString
public class MessageMediaResponse implements Serializable {

    private String id;

    private Integer ttl;

    private String fileUuid;

    private String fileInfo;

    private String fileLink;

}
