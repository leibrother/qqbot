package cc.rapidev.qqbot.api.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    private Integer fileType;

    public boolean isImage() {
        return fileType == 1;
    }

    public boolean isVideo() {
        return fileType == 2;
    }

    public boolean isAudio() {
        return fileType == 3;
    }

}
