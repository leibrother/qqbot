package cc.rapidev.qqbot.boot.plugin;

import cc.rapidev.qqbot.Bot;
import cc.rapidev.qqbot.boot.plugin.dependency.DependencyManager;
import cc.rapidev.qqbot.boot.plugin.exception.PluginNotFoundException;
import cc.rapidev.qqbot.message.MessageDispatcher;
import cc.rapidev.qqbot.message.MessageHandlerInjector;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author leibrother
 */
public class PluginManager {

    private final Bot bot;
    private final PluginFinder pluginFinder;
    private final PluginLoader pluginLoader;
    private final DependencyManager dependencyManager;

    public PluginManager(Bot bot, List<String> paths) {
        this.bot = bot;
        List<Path> exists = paths.stream().map(Path::of).filter(Files::exists).toList();
        this.pluginFinder = new PluginFinder(exists);
        this.pluginLoader = new PluginLoader();
        this.dependencyManager = new DependencyManager(pluginFinder);
    }

    public List<Plugin> plugins() {
        return this.pluginFinder.plugins();
    }

    public List<Plugin> enabled() {
        return this.pluginLoader.loaded();
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

    public List<Plugin> enable(List<Plugin> list) {
        List<Plugin> plugins = dependencyManager.resolve(list);
        MessageDispatcher dispatcher = bot.getDispatcher();
        for (Plugin plugin : plugins) {
            List<MessageHandlerInjector> injectors = pluginLoader.load(plugin);
            injectors.forEach(dispatcher::register);
        }
        return plugins;
    }

}
