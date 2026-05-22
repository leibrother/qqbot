package cc.rapidev.qqbot.launcher.plugin.exception;

import cc.rapidev.qqbot.common.VExpr;
import cc.rapidev.qqbot.launcher.plugin.Plugin;
import lombok.Getter;

/**
 * @author leibrother
 */
@Getter
public class IncompatibleException extends PluginException {

    private final Plugin plugin;
    private final Plugin dependency;
    private final VExpr expected;

    public IncompatibleException(Plugin plugin, Plugin dependency, VExpr expected) {
        this.plugin = plugin;
        this.dependency = dependency;
        this.expected = expected;
        super("plugin %s is incompatible with %s, expected version: %s".formatted(plugin, dependency, expected));
    }

}
