package cc.rapidev.qqbot.memory.repository.po;

import cc.rapidev.qqbot.database.entity.annotations.DBTable;
import cc.rapidev.qqbot.database.entity.annotations.TBColumn;
import cc.rapidev.qqbot.database.entity.annotations.TBPrimaryKey;
import cc.rapidev.qqbot.message.model.MessageAttachmentGeneric;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author leibrother
 */
@Getter
@Setter
@NoArgsConstructor
@DBTable(name = "memory_message_attachments")
public class MessageAttachmentPO {

    @TBColumn
    @TBPrimaryKey
    private String id;

    @TBColumn
    private String msgid;

    @TBColumn
    private String topic;

    @TBColumn
    private String filename;

    @TBColumn
    private String type;

    @TBColumn
    private String url;

    @TBColumn
    private Integer size;

    @TBColumn
    private Integer width;

    @TBColumn
    private Integer height;

    @TBColumn
    private String wavUrl;

    @TBColumn
    private String asrText;

    public MessageAttachmentPO(MessageAttachmentGeneric attachment, MessagePO message) {
        this.id = attachment.id();
        this.msgid = message.getId();
        this.topic = message.getTopic();
        this.filename = attachment.filename();
        this.type = attachment.type();
        this.url = attachment.url();
        this.size = attachment.size();
        this.width = attachment.width();
        this.height = attachment.height();
        this.wavUrl = attachment.wavUrl();
        this.asrText = attachment.asrText();
    }

    public MessageAttachmentGeneric toMemoryMessageAttachment() {
        return new MessageAttachmentGeneric(
                id,
                filename,
                type,
                url,
                size,
                width,
                height,
                wavUrl,
                asrText
        );
    }

}
