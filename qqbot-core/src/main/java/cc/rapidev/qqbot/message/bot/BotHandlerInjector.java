package cc.rapidev.qqbot.message.bot;

import cc.rapidev.qqbot.common.Events;
import cc.rapidev.qqbot.message.MessageDispatcher;
import cc.rapidev.qqbot.message.MessageHandlerInjector;

/**
 * @author leibrother
 */
public class BotHandlerInjector implements MessageHandlerInjector {

    @Override
    public void inject(MessageDispatcher dispatcher) {
        StartupListener startedHandler = new StartupListener();
        dispatcher.register(Events.START, startedHandler);
        dispatcher.register(Events.STARTED, startedHandler);
    }

}
