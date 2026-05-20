package cc.rapidev.qqbot.boot.plugin.dependency;

import cc.rapidev.qqbot.boot.plugin.Plugin;
import cc.rapidev.qqbot.boot.plugin.PluginFinder;
import cc.rapidev.qqbot.boot.plugin.exception.CircularDependencyException;
import cc.rapidev.qqbot.boot.plugin.exception.IncompatibleException;
import cc.rapidev.qqbot.boot.plugin.exception.PluginNotFoundException;
import cc.rapidev.qqbot.common.VExpr;

import java.util.*;

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

    /**
     * 分析给定插件的依赖，给出反转的插件列表（含本体）
     *
     * @param plugins 插件
     * @return 依赖列表
     */
    public List<Plugin> resolve(List<Plugin> plugins) {
        Deque<Plugin> stack = new ArrayDeque<>();
        for (Plugin plugin : plugins) {
            dfs(plugin, new ArrayList<>(), stack);
        }
        List<Plugin> ordered = new ArrayList<>(stack);
        return Collections.unmodifiableList(ordered.reversed());
    }

    /**
     * 搜索依赖路径
     *
     * @param plugin   查找的插件
     * @param visiting 当前已循环的插件ID，重复出现代表依赖循环
     * @param stack    反向依赖路径栈
     */
    private void dfs(Plugin plugin, List<Plugin> visiting, Deque<Plugin> stack) {
        if (visiting.contains(plugin)) {
            throw new CircularDependencyException(visiting);
        }
        if (stack.contains(plugin)) {
            return;
        }
        visiting.add(plugin);
        for (Map.Entry<String, VExpr> dependency : plugin.manifest().dependencies().entrySet()) {
            Plugin depend = pluginFinder.find(dependency.getKey()).orElseThrow(() -> new PluginNotFoundException(dependency.getKey()));
            if (!dependency.getValue().satisfy(depend.version())) {
                throw new IncompatibleException(plugin, depend, dependency.getValue());
            }
            dfs(depend, visiting, stack);
        }
        visiting.remove(plugin);
        stack.push(plugin);
    }

    public List<Plugin> dependOn(List<Plugin> plugins) {
        List<Plugin> dependencies = new ArrayList<>();
        for (Plugin plugin : plugins) {
            for (Plugin dependency : pluginFinder.plugins()) {
                if (!dependency.dependencies().containsKey(plugin.id())) {
                    continue;
                }
                List<Plugin> dependOn = dependOn(List.of(dependency));
                dependencies.addAll(dependOn);
                dependencies.add(dependency);
            }
        }
        return dependencies;
    }

}
