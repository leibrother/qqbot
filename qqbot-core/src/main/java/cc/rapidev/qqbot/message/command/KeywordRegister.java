package cc.rapidev.qqbot.message.command;

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
        dispatcher.register(Events.START, this);
    }

    @Override
    public void handle(MessageContext context) {
        CommandMessageHandler handler = context.getService(CommandMessageHandler.class);
        try {
            register(handler);
        } catch (IllegalArgumentException e) {
            logger.error("register keyword error", e);
        }
    }

    protected abstract void register(HierarchyCommandHandler handler);

}
