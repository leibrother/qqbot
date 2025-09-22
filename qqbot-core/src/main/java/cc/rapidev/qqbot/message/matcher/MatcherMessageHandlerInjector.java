package cc.rapidev.qqbot.message.matcher;

import cc.rapidev.qqbot.common.Events;
import cc.rapidev.qqbot.message.MessageDispatcher;
import cc.rapidev.qqbot.message.MessageHandlerInjector;

/**
 * @author leibrother
 */
public class MatcherMessageHandlerInjector implements MessageHandlerInjector {

    @Override
    public void inject(MessageDispatcher dispatcher) {
        MatcherMessageHandler handler = new MatcherMessageHandler();
        Events.messageCreateEvents.forEach(event -> dispatcher.register(event, handler));
    }

}
