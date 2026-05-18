package cc.rapidev.qqbot.message;

import cc.rapidev.qqbot.common.Events;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 默认的消息处理器实现
 *
 * @author leibrother
 */
public class NotImplMessageHandler implements MessageHandler {

    private final Logger log = LoggerFactory.getLogger("[Bot Message Dispatcher]");

    @Override
    public void handle(MessageContext context) {
        Events event = context.event();
        log.warn("no handler registered for event {}", event);
    }

}
