package cc.rapidev.qqbot.message.listener;

import cc.rapidev.qqbot.message.MessageContext;
import cc.rapidev.qqbot.message.MessageHandler;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * 打印机器人启动完成日志
 *
 * @author leibrother
 */
public class StartListener implements MessageHandler {

    private final Logger logger = LoggerFactory.getLogger(StartListener.class);
    private StopWatch stopWatch;

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
        logger.info("Bot starting...");
        if (stopWatch == null || stopWatch.isStopped()) {
            this.stopWatch = StopWatch.create();
            this.stopWatch.start();
        }
    }

    public long stopAndGetTime() {
        if (this.stopWatch == null) {
            return 0;
        } else if (this.stopWatch.isStarted()) {
            this.stopWatch.stop();
        }
        return this.stopWatch.getTime(TimeUnit.MILLISECONDS);
    }

}
