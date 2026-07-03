package cc.rapidev.qqbot.extension.push.handler;

import cc.rapidev.qqbot.common.Event;
import cc.rapidev.qqbot.extension.push.PushService;
import cc.rapidev.qqbot.message.MessageContext;
import cc.rapidev.qqbot.message.MessageDispatcher;
import cc.rapidev.qqbot.message.MessageHandler;
import cc.rapidev.qqbot.message.MessageHandlerInjector;

/**
 * @author leibrother
 */
public class AutoCloseHandler implements MessageHandler, MessageHandlerInjector {

    private final PushService service;

    public AutoCloseHandler(PushService service) {
        this.service = service;
    }

    @Override
    public int order() {
        return 1;
    }

    @Override
    public void handle(MessageContext context) {
        switch (context.event()) {
            case FRIEND_DEL, GUILD_DELETE, GROUP_DEL_ROBOT -> service.close(context.topic());
        }
    }

    @Override
    public void inject(MessageDispatcher dispatcher) {
        dispatcher.register(Event.FRIEND_DEL, this);
        dispatcher.register(Event.GROUP_DEL_ROBOT, this);
        dispatcher.register(Event.GUILD_DELETE, this);
    }

}
