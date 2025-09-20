package cc.rapidev.qqbot;

import cc.rapidev.qqbot.common.Config;
import cc.rapidev.qqbot.common.Constant;

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
        return getProperty(Constant.PROPERTY_APPID);
    }

    public String getSecret() {
        return getProperty(Constant.PROPERTY_SECRET);
    }

}
