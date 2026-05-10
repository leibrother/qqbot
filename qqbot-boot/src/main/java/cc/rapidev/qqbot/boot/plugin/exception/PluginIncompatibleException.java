package cc.rapidev.qqbot.boot.plugin.exception;

import lombok.Getter;

/**
 * @author leibrother
 */
@Getter
public class PluginIncompatibleException extends PluginException {

    private final String name;
    private final String currentVersion;
    private final String targetVersion;

    public PluginIncompatibleException(String name, String currentVersion, String targetVersion) {
        this.name = name;
        this.currentVersion = currentVersion;
        this.targetVersion = targetVersion;
        String message = "插件版本不兼容，当前版本: %s，目标版本: %s".formatted(currentVersion, targetVersion);
        super(message);
    }

}
