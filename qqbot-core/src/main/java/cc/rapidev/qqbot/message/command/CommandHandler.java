package cc.rapidev.qqbot.message.command;

import cc.rapidev.qqbot.message.MessageContext;

/**
 * @author leibrother
 */
public interface CommandHandler {

    void handle(MessageContext context, Command command);

}
