package cc.rapidev.qqbot.message.memory;

import java.util.List;

/**
 *
 * @author leibrother
 */
public class InMemoryMessageRepository implements MessageRepository {

    @Override
    public List<MemoryMessage> findByConversationId(String conversationId) {
        return List.of();
    }

    @Override
    public void save(String conversationId, MemoryMessage message) {

    }

    @Override
    public void save(String conversationId, List<MemoryMessage> messages) {

    }

    @Override
    public void deleteByConversationId(String conversationId) {

    }

    @Override
    public void deleteByConversationIdAndMessageId(String conversationId, String messageId) {

    }

}
