package cc.rapidev.qqbot.message.memory;

import cc.rapidev.qqbot.common.Topic;
import lombok.Getter;

import java.util.List;

/**
 * @author leibrother
 */
public class MemoryService {

    private final MessageRepository repository;
    @Getter
    private final Topic topic;
    @Getter
    private final MemoryMessage message;

    public MemoryService(MessageRepository repository, Topic topic, MemoryMessage message) {
        this.repository = repository;
        this.topic = topic;
        this.message = message;
    }

    public List<MemoryMessage> histories() {
        return repository.findByTopic(topic);
    }

    public void remember() {
        repository.save(topic, message);
    }

    public void forget() {
        repository.deleteByTopicAndMessageId(topic, message.getId());
    }

    public void forgetAll() {
        repository.deleteByTopic(topic);
    }

}
