package cc.rapidev.qqbot.boot.plugin.dependency;

import cc.rapidev.qqbot.Bot;
import cc.rapidev.qqbot.boot.plugin.Plugin;
import cc.rapidev.qqbot.boot.plugin.PluginFinder;
import cc.rapidev.qqbot.boot.plugin.exception.PluginDependencyException;
import cc.rapidev.qqbot.boot.plugin.exception.PluginIncompatibleException;
import cc.rapidev.qqbot.boot.plugin.exception.PluginNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * 插件依赖管理器
 *
 * @author leibrother
 */
public class DependencyManager {

    private final PluginFinder pluginFinder;

    public DependencyManager(PluginFinder pluginFinder) {
        this.pluginFinder = pluginFinder;
    }

    private void compareBotVersion(String expr) {
        if (!Bot.version.compare(expr)) {
            throw new PluginIncompatibleException("", Bot.version.toString(), expr);
        }
    }

    public Dependency dependencies(String id) {
        Plugin plugin = pluginFinder.find(id).orElseThrow(() -> new PluginNotFoundException(id));
        return dependencies(plugin, List.of());
    }

    public Dependency dependencies(Plugin plugin) {
        return dependencies(plugin, List.of());
    }

    private Dependency dependencies(Plugin plugin, List<Plugin> paths) {
        Map<String, String> depends = plugin.manifest().depends();
        List<Dependency> dependencies = new ArrayList<>();
        depends.forEach((id, expr) -> {
            // @bot 固定指 qqbot-core
            if ("@bot".equals(id)) {
                compareBotVersion(expr);
            } else {
                Plugin dependent = pluginFinder.find(id).orElseThrow(() -> new PluginNotFoundException(id));
                // 检查循环依赖
                int index = paths.indexOf(dependent);
                if (index >= 0) {
                    List<Plugin> looped = paths.subList(index + 1, paths.size());
                    throw PluginDependencyException.loops(looped);
                }
                // 比对版本
                if (!dependent.version().compare(expr)) {
                    throw new PluginIncompatibleException(dependent.name(), dependent.version().toString(), expr);
                }
                // 递归构建依赖树
                Dependency dependency = dependencies(dependent, Stream.concat(paths.stream(), Stream.of(dependent)).toList());
                dependencies.add(dependency);
            }
        });
        return new Dependency(plugin, dependencies);
    }

}
