package cc.rapidev.qqbot.boot.plugin;

import cc.rapidev.qqbot.boot.plugin.dependency.DependencyManager;
import cc.rapidev.qqbot.boot.plugin.exception.PluginNotFoundException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author leibrother
 */
public class PluginManager {

    private final PluginFinder pluginFinder;
    private final PluginLoader pluginLoader;
    private final DependencyManager dependencyManager;

    public PluginManager(List<String> paths) {
        List<Path> exists = paths.stream().map(Path::of).filter(Files::exists).toList();
        this.pluginFinder = new PluginFinder(exists);
        this.pluginLoader = new PluginLoader();
        this.dependencyManager = new DependencyManager(pluginFinder);
    }

    public List<Plugin> plugins() {
        return this.pluginFinder.plugins();
    }

    public Optional<Plugin> get(String id) {
        return this.pluginFinder.find(id);
    }

    public void enable(String... ids) {
        List<Plugin> list = new ArrayList<>();
        for (String id : ids) {
            Plugin plugin = get(id).orElseThrow(() -> new PluginNotFoundException(id));
            list.add(plugin);
        }
        enable(list);
    }

    public void enable(List<Plugin> list) {
        List<Plugin> plugins = dependencyManager.resolve(list);
        for (Plugin plugin : plugins) {
            pluginLoader.load(plugin);
        }
    }

}
