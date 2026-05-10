package cc.rapidev.qqbot.boot.plugin;

import cc.rapidev.qqbot.Bot;
import cc.rapidev.qqbot.boot.plugin.command.PluginCommandInjector;
import cc.rapidev.qqbot.boot.plugin.exception.PluginDependencyException;
import cc.rapidev.qqbot.boot.plugin.exception.PluginIncompatibleException;
import cc.rapidev.qqbot.boot.plugin.exception.PluginNotFoundException;
import org.jspecify.annotations.NonNull;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

/**
 * @author leibrother
 */
public class PluginService {

    private final Bot bot;
    private final List<Path> paths;
    private final Map<String, Plugin> plugins = new HashMap<>();

    public PluginService(Bot bot) {
        this.bot = bot;
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

    public Dependency dependencies(Plugin plugin) {
        return dependencies(plugin, List.of());
    }

    public Dependency dependencies(Plugin plugin, List<Plugin> paths) {
        List<Dependency> dependencies = new ArrayList<>();
        Map<String, String> depends = plugin.manifest().depends();
        depends.forEach((name, targetVersion) -> {
            // @bot 固定指 qqbot-core
            if ("@bot".equals(name)) {
                if (!Bot.version.match(targetVersion)) {
                    throw new PluginIncompatibleException(name, Bot.version.toString(), targetVersion);
                }
                return;
            }
            Plugin depend = get(name).orElseThrow(() -> new PluginNotFoundException(name));
            // 循环依赖检查 如果当前插件出现在了依赖路径中则表示发生了循环
            int index = paths.indexOf(depend);
            if (index != -1) {
                List<Plugin> looped = paths.subList(index + 1, paths.size());
                throw PluginDependencyException.loops(looped);
            }
            // 比对版本
            if (!depend.version().match(targetVersion)) {
                throw new PluginIncompatibleException(depend.name(), depend.version().toString(), targetVersion);
            }
            // 递归构建依赖树
            Dependency dependency = dependencies(depend, Stream.concat(paths.stream(), Stream.of(depend)).toList());
            dependencies.add(dependency);
        });
        return new Dependency(plugin, dependencies);
    }

    public void enable(@NonNull String name) {
        Plugin plugin = get(name).orElseThrow(() -> new PluginNotFoundException(name));
        // 构建依赖树
        Dependency dependency = dependencies(plugin);
        // 启用所有依赖项与本体
        for (Plugin item : dependency.successively()) {
            item.enable(bot);
        }
    }

    public static void init(Bot bot) {
        PluginService service = new PluginService(bot);
        PluginCommandInjector injector = new PluginCommandInjector(service);
        injector.inject(bot.getDispatcher());
    }

}
