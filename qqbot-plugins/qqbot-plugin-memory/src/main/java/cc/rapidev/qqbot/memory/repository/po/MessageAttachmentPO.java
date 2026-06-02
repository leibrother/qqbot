package cc.rapidev.qqbot.memory.repository.po;

import cc.rapidev.qqbot.database.entity.annotations.DBTable;
import cc.rapidev.qqbot.database.entity.annotations.TBColumn;
import cc.rapidev.qqbot.database.entity.annotations.TBPrimaryKey;
import cc.rapidev.qqbot.memory.model.MemoryMessageAttachment;
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

    public MessageAttachmentPO(MemoryMessageAttachment attachment, MessagePO message) {
        this.id = attachment.id();
        this.msgid = message.getId();
        this.topic = message.getTopic();
        this.filename = attachment.filename();
        this.type = attachment.type();
        this.url = attachment.url();
    }

    public MemoryMessageAttachment toMemoryMessageAttachment() {
        return new MemoryMessageAttachment(
                id,
                filename,
                type,
                url
        );
    }

}
