package cc.rapidev.qqbot.extension.command;

import cc.rapidev.qqbot.common.Events;
import cc.rapidev.qqbot.message.MessageContext;
import cc.rapidev.qqbot.message.MessageDispatcher;
import cc.rapidev.qqbot.message.MessageHandler;
import cc.rapidev.qqbot.message.MessageHandlerInjector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author leibrother
 */
public abstract class KeywordRegister implements MessageHandler, MessageHandlerInjector {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void inject(MessageDispatcher dispatcher) {
        dispatcher.register(Events.STARTED, this);
    }

    @Override
    public void handle(MessageContext context) {
        CommandEntry handler = context.getService(CommandEntry.class);
        try {
            register(handler);
        } catch (IllegalArgumentException e) {
            logger.error("failed to register keyword", e);
        }
    }

    protected abstract void register(CommandHandlerSet handler);

}
