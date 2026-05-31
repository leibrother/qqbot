package cc.rapidev.qqbot.agent.handler;

import cc.rapidev.qqbot.agent.service.AgentService;
import cc.rapidev.qqbot.common.Events;
import cc.rapidev.qqbot.message.MessageContext;
import cc.rapidev.qqbot.message.MessageDispatcher;
import cc.rapidev.qqbot.message.MessageHandler;
import cc.rapidev.qqbot.message.MessageHandlerInjector;

/**
 * @author leibrother
 */
public class AgentMessageHandler implements MessageHandler, MessageHandlerInjector {

    private final AgentService service;

    public AgentMessageHandler(AgentService service) {
        this.service = service;
    }

    @Override
    public int order() {
        return Integer.MAX_VALUE - 1000;
    }

    @Override
    public void handle(MessageContext context) {
        service.chat(context);
    }

    @Override
    public void inject(MessageDispatcher dispatcher) {
        Events.messageCreateEvents.forEach(e -> dispatcher.register(e, this));
    }

}
