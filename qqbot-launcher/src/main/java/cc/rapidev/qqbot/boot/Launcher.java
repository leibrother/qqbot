package cc.rapidev.qqbot.boot;

import cc.rapidev.qqbot.Bot;
import cc.rapidev.qqbot.BotConfig;
import cc.rapidev.qqbot.boot.plugin.PluginService;
import cc.rapidev.qqbot.common.Constant;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Stream;

/**
 * @author leibrother
 */
public class Launcher {

    public static void printWelcome() {
        System.out.println("---------------------------------------");
        System.out.println("Welcome Rapidev QQBot");
        System.out.println("Framework Version: " + Bot.version);
        System.out.println("请设置启动参数或者环境变量来配置你的机器人！");
        System.out.println("Appid:\t" + Constant.PROPERTY_APPID);
        System.out.println("Secret:\t" + Constant.PROPERTY_SECRET);
        System.out.println("---------------------------------------");
        System.out.println();
    }

    public static Properties getPropertiesByArgs(String[] args) {
        Properties properties = new Properties();
        if (args != null) {
            Optional<String> optional = Stream.of(args).filter(arg -> arg.startsWith("--config=")).findFirst();
            if (optional.isPresent()) {
                String path = optional.get().split("=")[1];
                File file = new File(path);
                if (file.exists()) {
                    try {
                        properties.load(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return properties;
    }

    static void main(String[] args) {
        printWelcome();
        Properties properties = getPropertiesByArgs(args);
        BotConfig config = BotConfig.create();
        config.loadProperties(properties);
        Bot bot = new Bot(config);
        PluginService.init(bot);
        bot.run();
    }

}
