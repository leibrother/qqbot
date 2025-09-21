package cc.rapidev.qqbot.message.handler;

import cc.rapidev.qqbot.common.Events;
import cc.rapidev.qqbot.message.MessageDispatcher;
import cc.rapidev.qqbot.message.MessageHandlerInjector;

/**
 * @author leibrother
 */
public class BotHandlerInjector implements MessageHandlerInjector {

    @Override
    public void inject(MessageDispatcher dispatcher) {
        BotStartedHandler startedHandler = new BotStartedHandler();
        dispatcher.register(Events.STARTED, startedHandler);
    }

}
