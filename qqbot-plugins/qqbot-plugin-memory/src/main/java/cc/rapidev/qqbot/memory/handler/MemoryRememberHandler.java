package cc.rapidev.qqbot.memory.handler;

import cc.rapidev.qqbot.memory.service.MemoryService;
import cc.rapidev.qqbot.message.MessageContext;
import cc.rapidev.qqbot.message.MessageHandler;

/**
 * @author leibrother
 */
public class MemoryRememberHandler implements MessageHandler {

    @Override
    public int order() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isRequired() {
        return true;
    }

    @Override
    public void handle(MessageContext context) {
        if (context.event().isMessageCreate()) {
            context.get(MemoryService.class).ifPresent(MemoryService::remember);
        }
    }

}
