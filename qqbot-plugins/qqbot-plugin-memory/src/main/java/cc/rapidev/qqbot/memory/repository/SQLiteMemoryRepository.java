package cc.rapidev.qqbot.memory.repository;

import cc.rapidev.qqbot.common.Topic;
import cc.rapidev.qqbot.database.BotDatabase;
import cc.rapidev.qqbot.database.entity.Table;
import cc.rapidev.qqbot.memory.model.MemoryMessage;
import cc.rapidev.qqbot.memory.repository.po.MessageAttachmentPO;
import cc.rapidev.qqbot.memory.repository.po.MessagePO;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.result.ResultIterable;
import org.jdbi.v3.core.statement.PreparedBatch;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author leibrother
 */
public class SQLiteMemoryRepository implements MemoryRepository {

    private final BotDatabase database;
    private final Table messageDef;
    private final Table attachmentDef;

    public SQLiteMemoryRepository(BotDatabase database) {
        this.database = database;
        this.messageDef = this.database.register(MessagePO.class);
        this.attachmentDef = this.database.register(MessageAttachmentPO.class);
    }

    @Override
    public void store(Topic topic, List<MemoryMessage> messages) {
        List<MessagePO> list = messages.stream()
                .map(message -> new MessagePO(message, topic))
                .toList();

        this.database.transaction(handle -> {
            this.clear(handle, topic);

            PreparedBatch batchMessage = handle.prepareBatch("""
                    INSERT INTO %s (id, sequence, topic, content, timestamp, bot)
                     VALUES(:id, :sequence, :topic, :content, :timestamp, :bot)
                    """.formatted(messageDef.name()));
            PreparedBatch batchAttachment = handle.prepareBatch("""
                    INSERT INTO %s (id, msgid, topic, filename, type, url, size, width, height, wav_url, asr_text)
                     VALUES(:id, :msgid, :topic, :filename, :type, :url, :size, :width, :height, :wavUrl, :asrText)
                    """.formatted(attachmentDef.name()));
            AtomicInteger index = new AtomicInteger(1);
            for (MessagePO po : list) {
                batchMessage.bindBean(po).bind("sequence", index.getAndIncrement()).add();
                po.getAttachments().forEach(attachment -> batchAttachment.bindBean(attachment).add());
            }
            batchMessage.execute();
            batchAttachment.execute();
        });
    }

    @Override
    public List<MemoryMessage> get(Topic topic) {
        return this.database.execute(handle -> {
            ResultIterable<MessagePO> messages = handle.select("""
                    SELECT * FROM %s WHERE topic = ? order by sequence asc
                    """.formatted(messageDef.name()), topic.toString()).mapToBean(MessagePO.class);
            ResultIterable<MessageAttachmentPO> attachments = handle.select("""
                    SELECT * FROM %s WHERE topic = ?
                    """.formatted(attachmentDef.name()), topic.toString()).mapToBean(MessageAttachmentPO.class);

            Map<String, List<MessageAttachmentPO>> map = attachments.stream()
                    .collect(Collectors.groupingBy(MessageAttachmentPO::getMsgid));

            return messages.stream()
                    .map(message -> {
                        message.setAttachments(map.getOrDefault(message.getId(), List.of()));
                        return message.toMemoryMessage();
                    })
                    .collect(Collectors.toList());
        });
    }

    @Override
    public void clear(Topic topic) {
        this.database.execute((handle) -> this.clear(handle, topic));
    }

    private int clear(Handle handle, Topic topic) {
        handle.execute("delete from %s where topic = ?".formatted(attachmentDef.name()), topic.toString());
        return handle.execute("delete from %s where topic = ?".formatted(messageDef.name()), topic.toString());
    }

}
