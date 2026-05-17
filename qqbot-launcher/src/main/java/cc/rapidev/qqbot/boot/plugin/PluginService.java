package cc.rapidev.qqbot.boot.plugin;

import cc.rapidev.qqbot.Bot;
import cc.rapidev.qqbot.boot.plugin.command.PluginKeywordRegister;
import cc.rapidev.qqbot.boot.plugin.dependency.Dependency;
import cc.rapidev.qqbot.boot.plugin.dependency.DependencyManager;
import cc.rapidev.qqbot.boot.plugin.exception.PluginNotFoundException;
import org.jspecify.annotations.NonNull;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author leibrother
 */
public class PluginService implements PluginFinder {

    private final Bot bot;
    private final PluginLoader pluginLoader;
    private final DependencyManager dependencyManager;
    private final Map<String, Plugin> plugins = new HashMap<>();

    public PluginService(Bot bot) {
        this.bot = bot;
        this.dependencyManager = new DependencyManager(this);
        String directory = bot.getConfig().getProperty("plugins", "./plugins");
        List<Path> paths = Stream.of(directory.split(",")).map(Paths::get).toList();
        this.pluginLoader = new PluginLoader(paths);
        for (Plugin plugin : pluginLoader.plugins()) {
            plugins.put(plugin.name(), plugin);
        }
    }

    public List<Plugin> plugins() {
        return this.plugins.values().stream().toList();
    }

    public Optional<Plugin> get(@NonNull String name) {
        Plugin plugin = this.plugins.get(name.toLowerCase());
        return Optional.ofNullable(plugin);
    }

    public void enable(@NonNull String name) {
        Plugin plugin = get(name).orElseThrow(() -> new PluginNotFoundException(name));
        // 构建依赖树
        Dependency dependency = dependencyManager.dependencies(plugin);
        // 启用所有依赖项与本体
    }

    public static void init(Bot bot) {
        PluginService service = new PluginService(bot);
        PluginKeywordRegister injector = new PluginKeywordRegister(service);
        injector.inject(bot.getDispatcher());
    }

    @Override
    public Optional<Plugin> find(String id) {
        Plugin plugin = this.plugins.get(id);
        return Optional.ofNullable(plugin);
    }

}
