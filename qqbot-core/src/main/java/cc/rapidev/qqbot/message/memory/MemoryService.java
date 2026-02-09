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
    private final List<MemoryMessage> cache = new ArrayList<>();
    private volatile List<MemoryMessage> remembered;

    public MemoryService(MessageRepository repository, Topic topic, MemoryMessage message) {
        this.repository = repository;
        this.topic = topic;
        this.message = message;
        this.add(message);
    }

    public void add(MemoryMessage message) {
        ObjectUtils._assert(message, "message must not be null");
        this.cache.add(message);
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
        if (this.remembered == null) {
            synchronized (this) {
                if (this.remembered == null) {
                    this.remembered = repository.find(topic);
                }
            }
        }
        return Stream.of(remembered, cache)
                .flatMap(Collection::stream)
                .sorted(MemoryMessage::compareTo)
                .toList();
    }

    /**
     * 记住当前消息
     */
    public synchronized void remember() {
        if (!this.cache.isEmpty()) {
            repository.save(topic, cache);
            if (this.remembered != null) {
                this.remembered.addAll(cache);
            }
            cache.clear();
        }
    }

    /**
     * 遗忘未记住的消息
     */
    public synchronized void forget() {
        if (!this.cache.isEmpty()) {
            this.cache.clear();
        }
    }

    /**
     * 遗忘所有消息
     */
    public synchronized void forgetAll() {
        this.forget();
        this.repository.remove(topic);
    }

}
