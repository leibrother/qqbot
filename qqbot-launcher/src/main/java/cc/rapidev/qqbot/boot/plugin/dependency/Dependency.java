package cc.rapidev.qqbot.boot.plugin.dependency;

import cc.rapidev.qqbot.boot.plugin.Plugin;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @author leibrother
 */
public record Dependency(Plugin plugin, List<Dependency> dependencies) {

    /**
     * 按照最底层到最上层的顺序列出插件
     *
     * @return 插件集合
     */
    public Set<Plugin> successively() {
        Set<Plugin> plugins = new LinkedHashSet<>();
        for (Dependency dependency : dependencies) {
            plugins.addAll(dependency.successively());
        }
        plugins.add(plugin);
        return plugins;
    }

}
