package cc.rapidev.qqbot.boot.plugin;

import cc.rapidev.qqbot.Bot;
import cc.rapidev.qqbot.boot.plugin.command.PluginKeywordRegister;
import cc.rapidev.qqbot.boot.plugin.exception.PluginNotFoundException;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Optional;

/**
 * @author leibrother
 */
public class PluginService {

    private final Bot bot;
    private final PluginManager mgr;

    public PluginService(Bot bot) {
        this.bot = bot;
        String[] paths = bot.getConfig().getProperty("plugins", "./plugins").split(",");
        this.mgr = new PluginManager(List.of(paths));
    }

    public List<Plugin> plugins() {
        return List.of();
    }

    public Optional<Plugin> get(@NonNull String name) {
        return Optional.empty();
    }

    public void enable(@NonNull String name) {
        Plugin plugin = get(name).orElseThrow(() -> new PluginNotFoundException(name));
    }

    public static void init(Bot bot) {
        PluginService service = new PluginService(bot);
        PluginKeywordRegister injector = new PluginKeywordRegister(service);
        injector.inject(bot.getDispatcher());
    }

}
