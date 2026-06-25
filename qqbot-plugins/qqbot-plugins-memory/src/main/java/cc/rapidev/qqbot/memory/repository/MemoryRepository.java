package cc.rapidev.qqbot.memory.repository;

import cc.rapidev.qqbot.common.Topic;
import cc.rapidev.qqbot.memory.model.MemoryMessage;

import java.util.List;

/**
 * @author leibrother
 */
public interface MemoryRepository {

    void store(Topic topic, List<MemoryMessage> messages);

    List<MemoryMessage> get(Topic topic);

    void clear(Topic topic);

}
