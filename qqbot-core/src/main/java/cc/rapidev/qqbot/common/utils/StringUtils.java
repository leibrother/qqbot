package cc.rapidev.qqbot.common.utils;

/**
 * @author leibrother
 */
public class StringUtils {

    private StringUtils() {
    }

    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static String orDefault(String str, String defaultStr) {
        return isEmpty(str) ? defaultStr : str;
    }

    public static String packing(CharSequence symbol, String content) {
        return packing(symbol, content, symbol);
    }

    public static String packing(CharSequence prefix, String content, CharSequence suffix) {
        return prefix + content + suffix;
    }

}
