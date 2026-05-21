package cc.rapidev.qqbot.extension.command;

import cc.rapidev.qqbot.message.MessageContext;
import cc.rapidev.qqbot.message.MessageHandler;
import cc.rapidev.qqbot.message.memory.MemoryMessage;
import cc.rapidev.qqbot.message.memory.MemoryService;

/**
 * @author leibrother
 */
public class CommandEntry extends CommandHandlerSet implements MessageHandler {

    @Override
    public int order() {
        return MessageHandler.super.order() - 100;
    }

    @Override
    public void handle(MessageContext context) {
        MemoryService memory = context.getService(MemoryService.class);
        MemoryMessage message = memory.current();
        Command command = new Command(message.getText());
        handle(context, command);
    }

}
