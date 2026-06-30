package cc.rapidev.qqbot.launcher.plugin;

import cc.rapidev.qqbot.common.utils.CastUtils;
import cc.rapidev.qqbot.extension.Extension;
import cc.rapidev.qqbot.launcher.plugin.cl.CompositeClassLoader;
import cc.rapidev.qqbot.launcher.plugin.cl.PluginClassLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.*;

/**
 * 插件加载器
 * <p>负责加载与卸载插件</p>
 *
 * @author leibrother
 */
public class PluginLoader implements Closeable {

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
     * @return 插件声明的所有扩展
     */
    public List<Class<? extends Extension>> load(Plugin plugin) {
        if (this.loaded.contains(plugin)) {
            logger.warn("plugin {} is already loaded, skipping", plugin.id());
        }
        ClassLoader dependencyClassLoader = getDependencyClassLoader(plugin);
        PluginClassLoader classLoader = new PluginClassLoader(plugin.urls(), dependencyClassLoader);
        List<Class<? extends Extension>> injectors = this.getExtensions(plugin, classLoader);
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
     * 获取插件所有的扩展
     *
     * @param plugin      插件
     * @param classLoader 插件的类加载器
     * @return injectors
     */
    private List<Class<? extends Extension>> getExtensions(Plugin plugin, ClassLoader classLoader) {
        List<Class<? extends Extension>> extensions = new ArrayList<>();
        for (String name : plugin.manifest().extensions()) {
            try {
                Class<?> clazz = classLoader.loadClass(name);
                if (!Extension.class.isAssignableFrom(clazz)) {
                    throw new ClassCastException("%s cannot be cast to %s".formatted(name, Extension.class.getName()));
                }
                extensions.add(CastUtils.cast(clazz));
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException("failed to load extension '%s' for plugin '%s'".formatted(name, plugin.id()), e);
            }
        }
        return extensions;
    }

    @Override
    public void close() {
        for (PluginClassLoader loader : this.classLoaders.values()) {
            try {
                loader.close();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
        this.loaded.clear();
        this.classLoaders.clear();
    }

}
