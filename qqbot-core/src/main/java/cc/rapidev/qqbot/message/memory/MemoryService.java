package cc.rapidev.qqbot.message.memory;

import cc.rapidev.qqbot.common.Topic;
import cc.rapidev.qqbot.message.memory.repository.MessageRepository;

import java.util.List;

/**
 * @author leibrother
 */
public class MemoryService {

    private final MessageRepository repository;
    private final Topic topic;
    private final MemoryMessage message;

    public MemoryService(MessageRepository repository, Topic topic, MemoryMessage message) {
        this.repository = repository;
        this.topic = topic;
        this.message = message;
    }

    /**
     * 记住当前消息
     */
    public void remember() {
        repository.save(topic, message);
    }

    /**
     * 获取当前消息
     *
     * @return 当前上下文的消息
     */
    public MemoryMessage current() {
        return this.message;
    }

    /**
     * 获取所有消息
     *
     * @return 当前上下文{@code topic}的所有消息
     */
    public List<MemoryMessage> all() {
        return repository.findByTopic(topic);
    }

    /**
     * 遗忘消息到当前消息之前(回滚到之前)
     */
    public void forget() {
        List<MemoryMessage> forgotten = all().stream().filter(message -> message.compareTo(this.message) >= 0).toList();
        forgotten.forEach(message -> repository.deleteByTopicAndMessageId(topic, message.getId()));
    }

    /**
     * 遗忘所有消息
     */
    public void forgetAll() {
        repository.deleteByTopic(topic);
    }

}
