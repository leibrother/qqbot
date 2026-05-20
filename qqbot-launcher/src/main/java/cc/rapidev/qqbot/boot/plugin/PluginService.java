package cc.rapidev.qqbot.boot.plugin;

import cc.rapidev.qqbot.Bot;
import cc.rapidev.qqbot.boot.plugin.command.PluginKeywordRegister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author leibrother
 */
public class PluginService {

    private final Logger logger = LoggerFactory.getLogger(PluginService.class);

    private final Bot bot;
    private final List<Plugin> disabled;
    private final PluginManager pluginManager;

    public PluginService(Bot bot) {
        this.bot = bot;
        this.disabled = new ArrayList<>();
        String[] paths = bot.getConfig().getProperty("plugins", "./plugins").split(",");
        this.pluginManager = new PluginManager(bot, List.of(paths));
        this.init();
    }

    /**
     * 从数据库获取已启用的插件，并启用他们
     */
    private void init() {
        List<String> ids = this.readDB();
        for (String id : ids) {
            Optional<Plugin> optional = pluginManager.get(id);
            if (optional.isEmpty()) {
                logger.warn("not found plugin {}, skipping", id);
            } else {
                this.pluginManager.enable(List.of(optional.get()));
            }
        }
        this.saveDB();
    }

    /**
     * 保存已启用的插件到数据库
     */
    private void saveDB() {
        String enabled = enabled().stream().map(Plugin::id).collect(Collectors.joining(","));
        bot.parameters().set("bot.plugins.enabled", enabled);
    }

    /**
     * 获取数据库中保存的已启用的插件
     *
     * @return 插件ID列表
     */
    private List<String> readDB() {
        Optional<Object> optional = bot.parameters().get("bot.plugins.enabled");
        if (optional.isPresent()) {
            String enabled = optional.get().toString();
            if (!enabled.isEmpty()) {
                return Arrays.stream(enabled.split(",")).toList();
            }
        }
        return List.of();
    }


    public Optional<Plugin> get(String id) {
        return this.pluginManager.get(id);
    }

    public List<Plugin> plugins() {
        return this.pluginManager.plugins();
    }

    public List<Plugin> enabled() {
        return this.pluginManager.enabled().stream().filter(plugin -> !disabled.contains(plugin)).toList();
    }

    public List<Plugin> disabled() {
        return this.disabled;
    }

    public List<Plugin> enable(String id) {
        Plugin plugin = get(id).orElseThrow();
        List<Plugin> enables = this.pluginManager.enable(List.of(plugin));
        this.disabled.remove(plugin);
        this.saveDB();
        return enables;
    }

    public void disable(String id) {
        Plugin plugin = get(id).orElseThrow();
        if (!this.disabled.contains(plugin)) {
            if (enabled().contains(plugin)) {
                this.disabled.add(plugin);
            }
        }
        this.saveDB();
    }

    public static void init(Bot bot) {
        PluginService service = new PluginService(bot);
        PluginKeywordRegister injector = new PluginKeywordRegister(service);
        injector.inject(bot.getDispatcher());
    }

}
