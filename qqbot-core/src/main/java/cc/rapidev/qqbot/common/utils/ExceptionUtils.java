package cc.rapidev.qqbot.common.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author leibrother
 */
public class ExceptionUtils {

    public static String getStackTrace(Throwable e) {
        StringWriter writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        e.printStackTrace(printWriter);
        return writer.toString();
    }

}
