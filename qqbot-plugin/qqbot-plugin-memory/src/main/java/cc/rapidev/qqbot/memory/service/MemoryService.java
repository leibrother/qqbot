package cc.rapidev.qqbot.memory.service;

import cc.rapidev.qqbot.common.Topic;
import cc.rapidev.qqbot.memory.model.MemoryMessage;
import cc.rapidev.qqbot.memory.repository.MemoryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author leibrother
 */
public class MemoryService {

    private final MemoryRepository repository;
    private final Topic topic;
    private final MemoryMessage message;
    private final List<MemoryMessage> remembered;
    private final List<MemoryMessage> temps;

    public MemoryService(MemoryRepository repository, Topic topic) {
        this(repository, topic, null);
    }

    public MemoryService(MemoryRepository repository, Topic topic, MemoryMessage message) {
        this.repository = repository;
        this.topic = topic;
        this.message = message;
        this.remembered = this.repository.get(topic);
        this.temps = new ArrayList<>();
        this.temps.add(this.message);
    }

    public MemoryMessage last() {
        if (this.remembered.isEmpty()) {
            return null;
        }
        return this.remembered.getLast();
    }

    public MemoryMessage current() {
        if (this.message == null) {
            throw new IllegalStateException();
        }
        return this.message;
    }

    public void add(MemoryMessage message) {
        this.temps.add(message);
    }

    public List<MemoryMessage> memory() {
        return Stream.concat(this.remembered.stream(), this.temps.stream()).toList();
    }

    public void forget() {
        this.repository.clear(this.topic);
        this.temps.clear();
        this.remembered.clear();
    }

    public void discard() {
        this.temps.clear();
    }

    public void remember() {
        if (!this.temps.isEmpty()) {
            this.repository.store(topic, this.memory());
            List<MemoryMessage> messages = repository.get(this.topic);
            this.temps.clear();
            this.remembered.clear();
            this.remembered.addAll(messages);
        }
    }

}
