package cc.rapidev.qqbot.common.utils;

/**
 * @author leibrother
 */
public class ObjectUtils {

    public static void _assert(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void _assert(boolean object, String message) {
        if (!object) {
            throw new IllegalArgumentException(message);
        }
    }

}
