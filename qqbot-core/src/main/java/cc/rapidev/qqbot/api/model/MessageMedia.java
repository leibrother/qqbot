package cc.rapidev.qqbot.api.model;

import lombok.*;

import java.io.Serializable;

/**
 * @author leibrother
 */
@Getter
@Setter(AccessLevel.PRIVATE)
@ToString
@NoArgsConstructor
public class MessageMedia implements Serializable {

    public MessageMedia(int fileType, String url) {
        this.fileType = fileType;
        this.url = url;
    }

    private Integer fileType;

    private String url;

    private Boolean srvSendMsg = false;

    public void srvSend() {
        this.srvSendMsg = true;
    }

    public void srvDontSend() {
        this.srvSendMsg = false;
    }

    public static MessageMedia image(String url) {
        return new MessageMedia(1, url);
    }

    public static MessageMedia video(String url) {
        return new MessageMedia(2, url);
    }

    public static MessageMedia audio(String url) {
        return new MessageMedia(3, url);
    }

}
