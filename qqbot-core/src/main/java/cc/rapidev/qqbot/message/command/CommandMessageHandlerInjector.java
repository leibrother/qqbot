package cc.rapidev.qqbot.message.command;

import cc.rapidev.qqbot.common.Events;
import cc.rapidev.qqbot.message.MessageDispatcher;
import cc.rapidev.qqbot.message.MessageHandlerInjector;

/**
 * @author leibrother
 */
public class CommandMessageHandlerInjector implements MessageHandlerInjector {

    @Override
    public void inject(MessageDispatcher dispatcher) {
        CommandMessageHandler handler = new CommandMessageHandler();
        dispatcher.register(Events.START, handler);
        dispatcher.register(Events.STARTED, handler);
        Events.messageCreateEvents.forEach(e -> dispatcher.register(e, handler));
    }

}
