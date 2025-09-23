package cc.rapidev.qqbot.message.memory.repository;

import cc.rapidev.qqbot.common.Topic;
import cc.rapidev.qqbot.message.memory.MemoryMessage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 *
 * @author leibrother
 */
public class InMemoryMessageRepository implements MessageRepository {

    private final Map<String, Map<String, MemoryMessage>> storage = new ConcurrentHashMap<>();

    @Override
    public List<MemoryMessage> findByTopic(Topic topic) {
        Map<String, MemoryMessage> topicMessages = storage.computeIfAbsent(topic.toString(), k -> new HashMap<>());
        return topicMessages.values()
                .stream()
                .sorted(MemoryMessage::compareTo)
                .collect(Collectors.toList());
    }

    @Override
    public void save(Topic topic, MemoryMessage message) {
        Map<String, MemoryMessage> topicMessages = storage.computeIfAbsent(topic.toString(), k -> new HashMap<>());
        topicMessages.put(message.getId(), message);
    }

    @Override
    public void save(Topic topic, List<MemoryMessage> messages) {
        Map<String, MemoryMessage> topicMessages = storage.computeIfAbsent(topic.toString(), k -> new HashMap<>());
        messages.forEach(message -> topicMessages.put(message.getId(), message));
    }

    @Override
    public void deleteByTopic(Topic topic) {
        storage.remove(topic.toString());
    }

    @Override
    public void deleteByTopicAndMessageId(Topic topic, String messageId) {
        Map<String, MemoryMessage> topicMessages = storage.computeIfAbsent(topic.toString(), k -> new HashMap<>());
        topicMessages.remove(messageId);
    }

}
