package cc.rapidev.qqbot.boot.plugin.exception;

import cc.rapidev.qqbot.boot.plugin.Plugin;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author leibrother
 */
public class PluginDependencyException extends PluginException {

    public PluginDependencyException(String message) {
        super(message);
    }

    public static PluginDependencyException loops(List<Plugin> paths) {
        String collect = Stream.concat(paths.stream(), Stream.of(paths.getFirst()))
                .map(Plugin::name)
                .collect(Collectors.joining(" -> "));
        return new PluginDependencyException("发生循环依赖：" + collect);
    }

}
