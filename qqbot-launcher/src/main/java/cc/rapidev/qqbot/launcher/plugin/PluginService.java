package cc.rapidev.qqbot.launcher.plugin;

import cc.rapidev.qqbot.Bot;
import cc.rapidev.qqbot.extension.Extension;
import cc.rapidev.qqbot.extension.command.CommandEntry;
import cc.rapidev.qqbot.launcher.plugin.command.PluginKeywordRegisterer;
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
public class PluginService implements Extension {

    private final Logger logger = LoggerFactory.getLogger(PluginService.class);

    private Bot bot;
    private List<Plugin> disabled;
    private PluginManager pluginManager;

    @Override
    public void ready(Bot bot) {
        this.bot = bot;
        this.disabled = new ArrayList<>();
        String[] paths = bot.getConfig().getProperty("plugins", "./plugins").split(",");
        this.pluginManager = new PluginManager(bot, List.of(paths));
        this.bot.use(CommandEntry.class).register(new PluginKeywordRegisterer(this));
        this.init();
    }

    @Override
    public void destroy() throws Exception {
        this.pluginManager.close();
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


    /**
     * 获取指定插件
     *
     * @param id 插件ID
     * @return Optional.ofNullable
     */
    public Optional<Plugin> get(String id) {
        return this.pluginManager.get(id);
    }

    /**
     * 获取全部插件
     */
    public List<Plugin> plugins() {
        return this.pluginManager.plugins();
    }

    /**
     * 获取已启用的插件
     */
    public List<Plugin> enabled() {
        return this.pluginManager.enabled().stream().filter(plugin -> !disabled.contains(plugin)).toList();
    }

    /**
     * 获取已禁用（待重启）的插件
     */
    public List<Plugin> disabled() {
        return this.disabled;
    }

    /**
     * 获取启用指定插件
     *
     * @param id 插件ID
     * @return 同时启用的依赖插件
     */
    public List<Plugin> enable(String id) {
        Plugin plugin = get(id).orElseThrow();
        List<Plugin> enables = this.pluginManager.enable(List.of(plugin));
        this.disabled.remove(plugin);
        this.saveDB();
        return enables.stream().filter(oth -> !oth.equals(plugin)).toList();
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

}
