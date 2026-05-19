package cc.rapidev.qqbot.boot.plugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 插件加载器
 * <p>负责加载与卸载插件</p>
 *
 * @author leibrother
 */
public class PluginLoader {

    private final Logger logger = LoggerFactory.getLogger(PluginLoader.class);

    private final Map<String, ClassLoader> classLoaders = new HashMap<>();

    private boolean isLoaded(Plugin plugin) {
        return this.classLoaders.containsKey(plugin.id());
    }

    public void load(Plugin plugin) {

    }

    public void unload(Plugin plugin) {
    }

}
