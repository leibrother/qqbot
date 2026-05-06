package cc.rapidev.qqbot.api.model;

import cc.rapidev.qqbot.api.response.MessageMediaResponse;
import lombok.*;

import java.io.Serializable;
import java.io.StringWriter;
import java.util.List;

/**
 * @author leibrother
 */
@Getter
@Setter(AccessLevel.PRIVATE)
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Message implements Serializable {

    private final static int msg_type_text = 0;
    private final static int msg_type_markdown = 2;
    private final static int msg_type_ark = 3;
    private final static int msg_type_media = 7;

    public Message(String content) {
        this.msgType = msg_type_text;
        this.content = content;
    }

    public Message(MessageMarkdown markdown) {
        this.msgType = msg_type_markdown;
        this.markdown = markdown;
    }

    public Message(MessageArk ark) {
        this.msgType = msg_type_ark;
        this.ark = ark;
    }

    public Message(MessageMediaResponse media) {
        this.msgType = msg_type_media;
        this.content = "";
        this.media = media;
    }

    private Integer msgType;

    private String content;

    private MessageMarkdown markdown;

    private MessageArk ark;

    private MessageMediaResponse media;

    private String eventId;

    private String msgId;

    private Integer msgSeq = 1;

    public void reply(String msgId, int msgSeq) {
        this.msgId = msgId;
        this.msgSeq = msgSeq;
    }

    public void contentReplace(String oldChar, String newChar) {
        if (this.content != null) {
            this.content = this.content.replace(oldChar, newChar);
        }
    }

    public boolean isText() {
        return this.msgType == msg_type_text;
    }

    public boolean isMarkdown() {
        return this.msgType == msg_type_markdown;
    }

    public boolean isArk() {
        return this.msgType == msg_type_ark;
    }

    public boolean isMedia() {
        return this.msgType == msg_type_media;
    }

    public static Message text(String content) {
        return new Message(content);
    }

    public static Message text(StringWriter writer) {
        return new Message(writer.toString());
    }

    public static Message markdown(String content) {
        MessageMarkdown markdown = new MessageMarkdown(content);
        return new Message(markdown);
    }

    public static Message markdown(String templateId, List<MessageMarkdown.MessageMarkdownParam> params) {
        MessageMarkdown markdown = new MessageMarkdown(templateId, params);
        return new Message(markdown);
    }

    public static Message ark(MessageArk ark) {
        return new Message(ark);
    }

    public static Message media(MessageMediaResponse media) {
        return new Message(media);
    }

}
