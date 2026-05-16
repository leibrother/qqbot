package cc.rapidev.qqbot.message.command;

import cc.rapidev.qqbot.common.Events;
import cc.rapidev.qqbot.message.MessageContext;
import cc.rapidev.qqbot.message.MessageHandler;
import cc.rapidev.qqbot.message.memory.MemoryMessage;
import cc.rapidev.qqbot.message.memory.MemoryService;

/**
 * @author leibrother
 */
public class CommandMessageHandler extends HierarchyCommandHandler implements MessageHandler {

    @Override
    public void handle(MessageContext context) {
        Events event = context.event();
        if (event == Events.START || event == Events.STARTED) {
            context.addService(this);
        } else {
            MemoryService memory = context.getService(MemoryService.class);
            MemoryMessage message = memory.current();
            Command command = new Command(message.getText());
            handle(context, command);
        }
    }

}
