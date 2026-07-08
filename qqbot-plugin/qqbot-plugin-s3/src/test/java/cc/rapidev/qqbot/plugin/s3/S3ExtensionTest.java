package cc.rapidev.qqbot.plugin.s3;

import cc.rapidev.qqbot.Bot;
import cc.rapidev.qqbot.BotConfig;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author leibrother
 */
public class S3ExtensionTest {

    @Test
    @Ignore
    public void run() throws IOException {
        File file = new File("../../data/qqbot.properties");
        Properties properties = new Properties();
        properties.load(new FileInputStream(file));
        BotConfig config = BotConfig.create();
        config.loadProperties(properties);
        Bot bot = new Bot(config);
        bot.install(S3Extension.class);
        bot.run();
    }

}