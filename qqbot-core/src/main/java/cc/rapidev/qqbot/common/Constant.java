package cc.rapidev.qqbot.common;

import java.time.format.DateTimeFormatter;

/**
 * @author leibrother
 */
public class Constant {

    private Constant() {
    }

    public final static String version = "1.0.0";

    public final static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");

    //---------------------------------------------------------
    // Payload Fields
    //---------------------------------------------------------
    public static final String PAYLOAD_ID = "id";
    public static final String PAYLOAD_OPCODE = "op";
    public static final String PAYLOAD_DATA = "d";
    public static final String PAYLOAD_SERIAL_NUMBER = "s";
    public static final String PAYLOAD_EVENT_TYPE = "t";

    //---------------------------------------------------------
    // Properties
    //---------------------------------------------------------
    public static final String PROPERTY_SANDBOX_ENABLE = "bot.sandbox.enable";
    public static final String PROPERTY_HOST = "bot.host";
    public static final String PROPERTY_APPID = "bot.appid";
    public static final String PROPERTY_SECRET = "bot.secret";
    public static final String PROPERTY_WEBHOOK_PORT = "bot.webhook.port";

    //---------------------------------------------------------
    // Properties Defaults
    //---------------------------------------------------------
    public static final String DEFAULT_PROPERTY_HOST = "api.sgroup.qq.com";
    public static final String DEFAULT_PROPERTY_SANDBOX_HOST = "sandbox.api.sgroup.qq.com";

}
