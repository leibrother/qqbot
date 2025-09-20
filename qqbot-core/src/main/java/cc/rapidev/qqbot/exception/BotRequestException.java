package cc.rapidev.qqbot.exception;

/**
 * @author leibrother
 */
public class BotRequestException extends BotException {

    public BotRequestException(String message) {
        super("API request error, message: %s ".formatted(message));
    }

    public BotRequestException(int code, String message) {
        super("API request error, code: %d, message: %s".formatted(code, message));
    }

}
