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
        StartListener start = new StartListener();
        StartedListener started = new StartedListener(start);
        dispatcher.register(Events.START, start);
        dispatcher.register(Events.STARTED, started);
    }

}
