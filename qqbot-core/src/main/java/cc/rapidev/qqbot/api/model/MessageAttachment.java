package cc.rapidev.qqbot.api.model;

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
public class MessageAttachment implements Serializable {

    private String url;

    public MessageAttachment(String url) {
        this.url = url;
    }

}
