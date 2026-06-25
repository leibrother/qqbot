package cc.rapidev.qqbot.memory.repository.po;

import cc.rapidev.qqbot.common.Topic;
import cc.rapidev.qqbot.common.utils.JsonUtils;
import cc.rapidev.qqbot.database.entity.annotations.DBTable;
import cc.rapidev.qqbot.database.entity.annotations.TBColumn;
import cc.rapidev.qqbot.database.entity.annotations.TBPrimaryKey;
import cc.rapidev.qqbot.memory.model.MemoryMessage;
import cc.rapidev.qqbot.memory.model.MemoryMessageAttachment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

/**
 * @author leibrother
 */
@Getter
@Setter
@NoArgsConstructor
@DBTable(name = "memory_messages")
public class MessagePO {

    @TBColumn
    @TBPrimaryKey
    private String id;

    @TBColumn
    private String topic;

    @TBColumn
    private String content;

    @TBColumn
    private Long timestamp;

    @TBColumn
    private boolean bot;

    @TBColumn
    private String metadata;

    @TBColumn
    private int sequence;

    private List<MessageAttachmentPO> attachments;

    public MessagePO(MemoryMessage message, Topic topic) {
        this.id = message.id();
        this.topic = topic.code();
        this.content = message.content();
        this.timestamp = message.timestamp().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        this.bot = message.bot();
        this.metadata = JsonUtils.toJson(message.metadata());
        if (message.attachments() == null || message.attachments().isEmpty()) {
            this.attachments = new ArrayList<>();
        } else {
            this.attachments = message.attachments()
                    .stream()
                    .map(attachment -> new MessageAttachmentPO(attachment, this))
                    .toList();
        }
    }

    public MemoryMessage toMemoryMessage() {
        List<MemoryMessageAttachment> attachments = this.attachments.stream()
                .map(MessageAttachmentPO::toMemoryMessageAttachment)
                .toList();

        return new MemoryMessage(
                id,
                content,
                attachments,
                LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault()),
                bot,
                JsonUtils.fromJson(this.metadata, JsonUtils.mapTypeReference())
        );
    }

}
