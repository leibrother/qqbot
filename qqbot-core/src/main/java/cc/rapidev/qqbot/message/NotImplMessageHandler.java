package cc.rapidev.qqbot.message;

import cc.rapidev.qqbot.common.Events;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author leibrother
 */
public class NotImplMessageHandler implements MessageHandler {

    private final Logger log = LoggerFactory.getLogger("[Bot Message Dispatcher]");

    @Override
    public void handle(MessageContext context) {
        Events event = context.getEvent();
        log.warn("event {} unregistered handler", event);
    }

}
