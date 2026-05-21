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
public class StartListener implements MessageHandler {

    private final Logger logger = LoggerFactory.getLogger(StartListener.class);

    private long startTimestamp;

    public long startTimestamp() {
        return this.startTimestamp;
    }

    @Override
    public int order() {
        return Integer.MIN_VALUE;
    }

    @Override
    public boolean isRequired() {
        return true;
    }

    @Override
    public void handle(MessageContext context) {
        this.startTimestamp = System.currentTimeMillis();
        logger.info("Bot starting...");
    }

}
