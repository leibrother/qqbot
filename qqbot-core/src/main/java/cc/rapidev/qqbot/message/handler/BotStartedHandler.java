package cc.rapidev.qqbot.message.handler;

import cc.rapidev.qqbot.message.MessageContext;
import cc.rapidev.qqbot.message.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 打印机器人启动完成日志
 *
 * @author leibrother
 */
public class BotStartedHandler implements MessageHandler {

    private final Logger logger = LoggerFactory.getLogger(BotStartedHandler.class);

    @Override
    public int order() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean must() {
        return true;
    }

    @Override
    public void handle(MessageContext context) {
        logger.info("Bot started");
    }

}
