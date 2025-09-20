package cc.rapidev.qqbot.api.model;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author leibrother
 */
@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageMarkdown implements Serializable {

    @Getter
    @Setter
    public static class MessageMarkdownParam implements Serializable {
        String key;
        List<String> values;
    }

    public MessageMarkdown(String content) {
        this.content = content;
    }

    public MessageMarkdown(String customTemplateId, List<MessageMarkdownParam> params) {
        this.customTemplateId = customTemplateId;
        this.params = params;
    }

    private String content;

    private String customTemplateId;

    private List<MessageMarkdownParam> params;


}
