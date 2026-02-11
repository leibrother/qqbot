package cc.rapidev.qqbot.message.memory;

import cc.rapidev.qqbot.common.Events;
import cc.rapidev.qqbot.message.MessageDispatcher;
import cc.rapidev.qqbot.message.MessageHandlerInjector;

/**
 * @author leibrother
 */
public class MemoryHandlerInjector implements MessageHandlerInjector {

    @Override
    public void inject(MessageDispatcher dispatcher) {
        MemoryHandler handler = new MemoryHandler();
        Events.messageCreateEvents.forEach(event -> dispatcher.register(event, handler));
        MemoryRememberHandler rememberHandler = new MemoryRememberHandler();
        Events.messageCreateEvents.forEach(event -> dispatcher.register(event, rememberHandler));
    }

}
