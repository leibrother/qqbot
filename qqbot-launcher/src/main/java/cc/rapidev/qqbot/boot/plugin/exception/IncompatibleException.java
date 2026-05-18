package cc.rapidev.qqbot.boot.plugin.exception;

import cc.rapidev.qqbot.boot.plugin.Plugin;
import cc.rapidev.qqbot.common.VExpr;
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
        super("plugin %s is not compatible with the version of %s, expected version: %s".formatted(plugin, dependency, expected));
    }

}
