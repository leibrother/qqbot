package cc.rapidev.qqbot.message.memory;

import cc.rapidev.qqbot.common.Topic;

import java.util.List;

/**
 * @author leibrother
 */
public interface MessageRepository {

    List<MemoryMessage> findByTopic(Topic topic);

    void save(Topic topic, MemoryMessage message);

    void save(Topic topic, List<MemoryMessage> messages);

    void deleteByTopic(Topic topic);

    void deleteByTopicAndMessageId(Topic topic, String messageId);

}
