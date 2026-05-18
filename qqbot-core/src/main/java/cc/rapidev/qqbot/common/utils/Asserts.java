package cc.rapidev.qqbot.common.utils;

/**
 * @author leibrother
 */
public class Asserts {

    public static void notnull(Object object, String msg) {
        if (object == null) {
            throw new IllegalStateException(msg);
        }
    }

    public static void notempty(String string, String msg) {
        if (string == null || string.trim().isEmpty()) {
            throw new IllegalStateException(msg);
        }
    }

    public static void isTrue(boolean bool, String msg) {
        if (!bool) {
            throw new IllegalStateException(msg);
        }
    }

    public static void isFalse(boolean bool, String msg) {
        if (bool) {
            throw new IllegalStateException(msg);
        }
    }

}
