package cc.rapidev.qqbot.common.utils;

import ch.qos.logback.classic.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author leibrother
 */
public class LogbackUtils {

    public static void setLogLevel(String name, Level level) {
        Logger logger = LoggerFactory.getLogger(name);
        if (logger instanceof ch.qos.logback.classic.Logger logbackLogger) {
            logbackLogger.setLevel(level);
        }
    }

}
