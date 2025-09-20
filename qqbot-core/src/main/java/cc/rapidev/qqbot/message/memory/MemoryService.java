package cc.rapidev.qqbot.message.memory;

import lombok.Getter;

import java.util.List;

/**
 * @author leibrother
 */
public class MemoryService {

    private final MessageRepository repository;
    private final String conversationId;
    @Getter
    private final MemoryMessage message;

    public MemoryService(MessageRepository repository, String conversationId, MemoryMessage message) {
        this.repository = repository;
        this.conversationId = conversationId;
        this.message = message;
    }

    public List<MemoryMessage> histories() {
        return repository.findByConversationId(conversationId);
    }

    public void store() {
        repository.save(conversationId, message);
    }

    public void forget() {
        repository.deleteByConversationIdAndMessageId(conversationId, message.getId());
    }

    public void forgetAll() {
        repository.deleteByConversationId(conversationId);
    }

}
