package cc.rapidev.qqbot.extension.command;

import cc.rapidev.qqbot.message.MessageContext;

/**
 * @author leibrother
 */
@FunctionalInterface
public interface CommandHandler {

    void handle(MessageContext context, Command command);

}
