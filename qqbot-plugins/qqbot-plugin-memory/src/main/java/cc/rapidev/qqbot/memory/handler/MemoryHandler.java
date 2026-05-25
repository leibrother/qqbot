package cc.rapidev.qqbot.memory.handler;

import cc.rapidev.qqbot.common.Topic;
import cc.rapidev.qqbot.memory.model.MemoryMessage;
import cc.rapidev.qqbot.memory.repository.MemoryRepository;
import cc.rapidev.qqbot.memory.service.MemoryService;
import cc.rapidev.qqbot.message.MessageContext;
import cc.rapidev.qqbot.message.MessageHandler;

/**
 * @author leibrother
 */
public class MemoryHandler implements MessageHandler {

    private final MemoryRepository repository;

    public MemoryHandler(MemoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public int order() {
        return 0;
    }

    @Override
    public boolean isRequired() {
        return true;
    }

    @Override
    public void handle(MessageContext context) {
        if (!context.event().isMessageCreate()) {
            return;
        }
        Topic topic = context.topic();
        if (topic == null) {
            return;
        }
        MemoryService service;
        if (context.message() == null) {
            service = new MemoryService(repository, topic);
        } else {
            service = new MemoryService(repository, topic, MemoryMessage.of(context.message()));
        }
        context.addService(service);
        addReplyHook(context);
    }

    private void addReplyHook(MessageContext context) {
        context.addReplyHook((message, response) -> {
            if (message == null || response == null) {
                return;
            }
            MemoryService service = context.getService(MemoryService.class);
            service.add(MemoryMessage.of(message, response));
        });
    }

}
