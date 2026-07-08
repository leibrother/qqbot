package cc.rapidev.qqbot;

import cc.rapidev.qqbot.common.Config;
import cc.rapidev.qqbot.common.Constant;
import cc.rapidev.qqbot.common.utils.StringUtils;
import cc.rapidev.qqbot.exception.PropertyException;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author leibrother
 */
public final class BotConfig extends Config {

    public static BotConfig create() {
        return new BotConfig();
    }

    public boolean sandbox() {
        return getPropertyAsBoolean(Constant.PROPERTY_SANDBOX_ENABLE, false);
    }

    public String getHost() {
        String property = getProperty(Constant.PROPERTY_HOST);
        if (property == null) {
            if (sandbox()) {
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

    public String getFeatures() {
        return getProperty(Constant.PROPERTY_FEATURES, Constant.DEFAULT_FEATURES);
    }

    public Path getDatadir() {
        String property = getProperty(Constant.PROPERTY_DATADIR, Constant.DEFAULT_DATADIR);
        try {
            Path path = Paths.get(property);
            return Files.createDirectories(path);
        } catch (IOException e) {
            throw new PropertyException(Constant.PROPERTY_DATADIR, e.getMessage());
        }
    }

    public int getServerPort() {
        int port = getPropertyAsInt(Constant.PROPERTY_SERVER_PORT, Integer.parseInt(Constant.DEFAULT_SERVER_PORT));
        if (port < 1 || port > 65535) {
            throw new PropertyException(Constant.PROPERTY_SERVER_PORT, "server port must be between 1 and 65535");
        }
        return port;
    }

    public URI getServerAccessibleUri() {
        String property = getProperty(Constant.PROPERTY_SERVER_ACCESSIBLE_URI);
        if (StringUtils.isEmpty(property)) {
            return null;
        }
        return URI.create(property);
    }

}
