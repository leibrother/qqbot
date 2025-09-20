package cc.rapidev.qqbot.api.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author leibrother
 */
@Getter
@Setter
@ToString
public class MessageArk implements Serializable {

    @Getter
    @Setter
    public static class MessageArkKV implements Serializable {
        private String key;
        private String value;
    }

    private String templateId;

    private List<MessageArkKV> kv = new ArrayList<>();

}
