package cc.rapidev.qqbot.message.memory;

import java.util.List;

/**
 * @author leibrother
 */
public interface MessageRepository {

    List<MemoryMessage> findByConversationId(String conversationId);

    void save(String conversationId, MemoryMessage message);

    void save(String conversationId, List<MemoryMessage> messages);

    void deleteByConversationId(String conversationId);

    void deleteByConversationIdAndMessageId(String conversationId, String messageId);

}
