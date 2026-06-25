package cc.rapidev.qqbot.memory;

import cc.rapidev.qqbot.Bot;
import cc.rapidev.qqbot.BotConfig;
import org.junit.Test;

/**
 * @author leibrother
 */
public class MemoryTest {

    @Test
    public void test() {
        BotConfig config = BotConfig.create();
        config.setProperty("bot.appid", "102343716");
        config.setProperty("bot.secret", "9Tn7Rl6Rm7Sn8UqCYuGd0Nk7UrFd1PnB");
        config.setProperty("bot.sandbox.enable", "true");
        Bot bot = new Bot(config);
        bot.install(MemoryExtension.class);
        bot.run();
    }

}