package cc.rapidev.qqbot.message.matcher;

import cc.rapidev.qqbot.message.MessageContext;
import cc.rapidev.qqbot.message.MessageHandler;
import cc.rapidev.qqbot.message.memory.MemoryMessage;
import cc.rapidev.qqbot.message.memory.MemoryService;

/**
 * <p>消息匹配处理器</p>
 * <p>将向{@link MessageContext}中注入{@link MatcherService}</p>
 *
 * @author leibrother
 */
public class MatcherHandler implements MessageHandler {

    @Override
    public int order() {
        return 1;
    }

    @Override
    public void handle(MessageContext context) {
        MemoryService memoryService = context.getService(MemoryService.class);
        MemoryMessage message = memoryService.current();
        MatcherService matcherService = new MatcherService(message.getText());
        context.addService("matcherService", matcherService);
    }

}
