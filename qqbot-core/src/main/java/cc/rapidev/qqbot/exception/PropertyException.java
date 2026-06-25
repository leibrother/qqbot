package cc.rapidev.qqbot.exception;

/**
 * @author leibrother
 */
public class PropertyException extends RuntimeException {

    public PropertyException(String key, String message) {
        super("bot property error: %s [%s]".formatted(message, key));
    }

}
