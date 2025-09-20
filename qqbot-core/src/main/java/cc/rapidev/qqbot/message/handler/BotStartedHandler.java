package cc.rapidev.qqbot.message.handler;

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
public class BotStartedHandler implements MessageHandler, MessageHandlerInjector {

    private final Logger logger = LoggerFactory.getLogger(BotStartedHandler.class);

    @Override
    public int order() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean must() {
        return true;
    }

    @Override
    public void inject(MessageDispatcher dispatcher) {
        dispatcher.register(Events.STARTED, this);
    }

    @Override
    public void handle(MessageContext context) {
        logger.info("Bot started");
    }

}
