package cc.rapidev.qqbot.extension.listener;

import cc.rapidev.qqbot.message.MessageContext;
import cc.rapidev.qqbot.message.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 打印机器人启动完成日志
 *
 * @author leibrother
 */
public class StartedListener implements MessageHandler {

    private final Logger logger = LoggerFactory.getLogger(StartedListener.class);

    private final StartListener start;

    public StartedListener(StartListener start) {
        this.start = start;
    }

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
        long startedTimestamp = System.currentTimeMillis();
        logger.info("Bot started in {}ms", startedTimestamp - start.startTimestamp());
    }

}
