package cc.rapidev.qqbot.extension.command;

import cc.rapidev.qqbot.message.MessageContext;
import cc.rapidev.qqbot.message.MessageHandler;
import cc.rapidev.qqbot.message.model.MessageGeneric;

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
        MessageGeneric message = context.message();
        Command command = new Command(message.content());
        handle(context, command);
    }

}
