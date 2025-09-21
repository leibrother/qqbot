package cc.rapidev.qqbot.message.memory;

import cc.rapidev.qqbot.common.Events;
import cc.rapidev.qqbot.message.MessageDispatcher;
import cc.rapidev.qqbot.message.MessageHandlerInjector;

/**
 * @author leibrother
 */
public class MemoryMessageHandlerInjector implements MessageHandlerInjector {

    @Override
    public void inject(MessageDispatcher dispatcher) {
        MemoryMessageHandler handler = new MemoryMessageHandler();
        Events.messageCreateEvents.forEach(event -> dispatcher.register(event, handler));
    }

}
