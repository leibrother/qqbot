package cc.rapidev.qqbot.launcher.plugin.exception;

import cc.rapidev.qqbot.launcher.plugin.Plugin;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author leibrother
 */
public class CircularDependencyException extends PluginException {

    public CircularDependencyException(List<Plugin> plugins) {
        String path = plugins.stream().map(Plugin::toString).collect(Collectors.joining(" -> "));
        super("detected plugin dependency cycle: " + path);
    }

}
