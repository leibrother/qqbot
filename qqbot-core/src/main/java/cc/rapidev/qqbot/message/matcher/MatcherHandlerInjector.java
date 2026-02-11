package cc.rapidev.qqbot.message.matcher;

import cc.rapidev.qqbot.common.Events;
import cc.rapidev.qqbot.message.MessageDispatcher;
import cc.rapidev.qqbot.message.MessageHandlerInjector;

/**
 * @author leibrother
 */
public class MatcherHandlerInjector implements MessageHandlerInjector {

    @Override
    public void inject(MessageDispatcher dispatcher) {
        MatcherHandler handler = new MatcherHandler();
        Events.messageCreateEvents.forEach(event -> dispatcher.register(event, handler));
    }

}
