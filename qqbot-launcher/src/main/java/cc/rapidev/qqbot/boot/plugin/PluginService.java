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
    private final List<Path> paths;
    private final DependencyManager dependencyManager;
    private final Map<String, Plugin> plugins = new HashMap<>();

    public PluginService(Bot bot) {
        this.bot = bot;
        this.dependencyManager = new DependencyManager(this);
        String plugins = bot.getConfig().getProperty("plugins", "./plugins");
        this.paths = Stream.of(plugins.split(",")).map(Paths::get).toList();
        scan();
    }

    public void scan() {
        Map<String, Plugin> plugins = new HashMap<>();
        for (Path path : this.paths) {
            List<Plugin> list = PluginLoader.search(path);
            for (Plugin plugin : list) {
                plugins.put(plugin.name().toLowerCase(), plugin);
            }
        }
        this.plugins.clear();
        this.plugins.putAll(plugins);
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
        for (Plugin item : dependency.successively()) {
            item.enable(bot);
        }
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
