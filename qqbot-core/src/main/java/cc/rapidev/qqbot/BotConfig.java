package cc.rapidev.qqbot;

import cc.rapidev.qqbot.common.Config;
import cc.rapidev.qqbot.common.Constant;
import cc.rapidev.qqbot.common.utils.StringUtils;
import cc.rapidev.qqbot.exception.PropertyException;

/**
 * @author leibrother
 */
public final class BotConfig extends Config {

    public static BotConfig create() {
        return new BotConfig();
    }

    public boolean isSandbox() {
        return getPropertyAsBoolean(Constant.PROPERTY_SANDBOX_ENABLE, false);
    }

    public String getHost() {
        String property = getProperty(Constant.PROPERTY_HOST);
        if (property == null) {
            if (isSandbox()) {
                return Constant.DEFAULT_PROPERTY_SANDBOX_HOST;
            } else {
                return Constant.DEFAULT_PROPERTY_HOST;
            }
        }
        return property;
    }

    public String getAppid() {
        String property = getProperty(Constant.PROPERTY_APPID);
        if (StringUtils.isEmpty(property)) {
            throw new PropertyException(Constant.PROPERTY_APPID, "please set bot appid");
        }
        return property;
    }

    public String getSecret() {
        String property = getProperty(Constant.PROPERTY_SECRET);
        if (StringUtils.isEmpty(property)) {
            throw new PropertyException(Constant.PROPERTY_SECRET, "please set bot secret");
        }
        return property;
    }

    public int getServerPort() {
        int port = getPropertyAsInt(Constant.PROPERTY_SERVER_PORT, Constant.DEFAULT_SERVER_PORT);
        if (port < 1 || port > 65535) {
            throw new PropertyException(Constant.PROPERTY_SERVER_PORT, "server port must be between 1 and 65535");
        }
        return port;
    }

}
