package cc.rapidev.qqbot.boot.plugin;

import cc.rapidev.qqbot.boot.plugin.cl.CompositeClassLoader;
import cc.rapidev.qqbot.boot.plugin.cl.PluginClassLoader;
import cc.rapidev.qqbot.message.MessageHandlerInjector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.*;

/**
 * 插件加载器
 * <p>负责加载与卸载插件</p>
 *
 * @author leibrother
 */
public class PluginLoader {

    private final Logger logger = LoggerFactory.getLogger(PluginLoader.class);

    private final List<Plugin> loaded;
    private final Map<String, PluginClassLoader> classLoaders;

    public PluginLoader() {
        this.loaded = new ArrayList<>();
        this.classLoaders = new HashMap<>();
    }

    /**
     * 获取已经加载的插件列表
     *
     * @return 状态为LOADED的插件列表
     */
    public List<Plugin> loaded() {
        return Collections.unmodifiableList(loaded);
    }

    /**
     * 加载插件
     *
     * @param plugin 插件
     * @return 消息处理器注入器列表
     */
    public List<MessageHandlerInjector> load(Plugin plugin) {
        if (this.loaded.contains(plugin)) {
            logger.warn("plugin {} is already loaded, skipping", plugin.id());
        }
        ClassLoader dependencyClassLoader = getDependencyClassLoader(plugin);
        PluginClassLoader classLoader = new PluginClassLoader(plugin.urls(), dependencyClassLoader);
        List<MessageHandlerInjector> injectors = this.getInjectors(plugin, classLoader);
        this.classLoaders.put(plugin.id(), classLoader);
        this.loaded.add(plugin);
        return injectors;
    }

    /**
     * 获取插件依赖的ClassLoader
     *
     * @param plugin 插件
     * @return ClassLoader
     */
    private ClassLoader getDependencyClassLoader(Plugin plugin) {
        List<ClassLoader> delegates = new ArrayList<>();
        for (String dep : plugin.dependencies().keySet()) {
            ClassLoader depClassLoader = this.classLoaders.get(dep);
            if (depClassLoader == null) {
                throw new IllegalStateException("plugin %s depends on dependency %s not loaded".formatted(plugin.id(), dep));
            }
            delegates.add(depClassLoader);
        }
        if (delegates.isEmpty()) {
            return this.getClass().getClassLoader();
        } else {
            return new CompositeClassLoader(delegates);
        }
    }

    /**
     * 获取所有的injector
     *
     * @param plugin      插件
     * @param classLoader 插件的类加载器
     * @return injectors
     */
    private List<MessageHandlerInjector> getInjectors(Plugin plugin, ClassLoader classLoader) {
        List<MessageHandlerInjector> injectors = new ArrayList<>();
        for (String name : plugin.manifest().injectors()) {
            try {
                Class<?> clazz = classLoader.loadClass(name);
                if (!MessageHandlerInjector.class.isAssignableFrom(clazz)) {
                    throw new ClassCastException("%s cannot be cast to %s".formatted(name, MessageHandlerInjector.class.getName()));
                }
                Constructor<?> constructor = clazz.getDeclaredConstructor();
                constructor.setAccessible(true);
                MessageHandlerInjector injector = (MessageHandlerInjector) constructor.newInstance();
                injectors.add(injector);
                logger.debug("plugin {} injector {} loaded", plugin.id(), name);
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException("failed to load injector '%s' for plugin '%s'".formatted(name, plugin.id()), e);
            }
        }
        return injectors;
    }

}
