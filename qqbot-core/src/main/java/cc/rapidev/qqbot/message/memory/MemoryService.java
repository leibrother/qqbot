package cc.rapidev.qqbot.message.memory;

import cc.rapidev.qqbot.common.Topic;
import cc.rapidev.qqbot.common.utils.ObjectUtils;
import cc.rapidev.qqbot.message.memory.repository.MessageRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author leibrother
 */
public class MemoryService {

    private final MessageRepository repository;
    private final Topic topic;
    private final MemoryMessage message;
    private final List<MemoryMessage> temporary = new ArrayList<>();
    private final List<MemoryMessage> remembered = new ArrayList<>();
    private boolean forgotten = false;

    public MemoryService(MessageRepository repository, Topic topic, MemoryMessage message) {
        this.repository = repository;
        this.topic = topic;
        this.message = message;
        temporary.add(message);
    }

    public void addReply(MemoryMessage message) {
        ObjectUtils._assert(message, "message must not be null");
        ObjectUtils._assert(message.isBot(), "message is not from a bot");
        temporary.add(message);
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
        List<MemoryMessage> remembered = repository.findByTopic(topic);
        return Stream.of(remembered, temporary)
                .flatMap(Collection::stream)
                .sorted(MemoryMessage::compareTo)
                .toList();
    }

    /**
     * 记住当前消息
     */
    public void remember() {
        if (forgotten) {
            return;
        }
        synchronized (this) {
            if (!temporary.isEmpty()) {
                List<MemoryMessage> temp = List.copyOf(temporary);
                repository.save(topic, temp);
                remembered.addAll(temporary);
                temporary.clear();
            }
        }
    }

    /**
     * 遗忘消息到当前消息之前(回滚到之前)
     */
    public void forget() {
        if (forgotten) {
            return;
        }
        synchronized (this) {
            this.forgotten = true;
            remembered.forEach(msg -> repository.deleteByTopicAndMessageId(topic, msg.getId()));
            remembered.clear();
        }
    }

    /**
     * 遗忘所有消息
     */
    public void forgetAll() {
        if (forgotten) {
            return;
        }
        synchronized (this) {
            repository.deleteByTopic(topic);
            temporary.clear();
            remembered.clear();
        }
    }

}
