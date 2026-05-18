package cc.rapidev.qqbot.message.bot;

import cc.rapidev.qqbot.common.Events;
import cc.rapidev.qqbot.message.MessageContext;
import cc.rapidev.qqbot.message.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 打印机器人启动完成日志
 *
 * @author leibrother
 */
public class StartupListener implements MessageHandler {

    private final Logger logger = LoggerFactory.getLogger(StartupListener.class);

    private long startupTimestamp;

    @Override
    public int order() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isRequired() {
        return true;
    }

    @Override
    public void handle(MessageContext context) {
        if (context.event() == Events.START) {
            startupTimestamp = System.currentTimeMillis();
            logger.info("Bot starting...");
        } else if (context.event() == Events.STARTED) {
            long startedTimestamp = System.currentTimeMillis();
            logger.info("Bot started in {}ms", startedTimestamp - startupTimestamp);
        }
    }

}
