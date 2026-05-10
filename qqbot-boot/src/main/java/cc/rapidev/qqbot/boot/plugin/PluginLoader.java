package cc.rapidev.qqbot.boot.plugin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * 插件加载器
 * <p>加载指定目录下声明了<code>plugin.json</code>文件的JAR包</p>
 *
 * @author leibrother
 */
public class PluginLoader {

    /**
     * 获取指定路径下的插件列表
     *
     * @param path 文件夹或Jar文件路径
     * @return 插件列表
     */
    public static List<Plugin> search(Path path) {
        List<Path> jars = searchJars(path);
        List<Plugin> plugins = new ArrayList<>();
        for (Path jar : jars) {
            try {
                Plugin plugin = new Plugin(jar);
                plugins.add(plugin);
            } catch (IOException ignore) {
            }
        }
        return plugins;
    }

    /**
     * 搜索JAR
     *
     * @param path 文件夹或文件路径
     * @return 所有的JAR路径
     */
    public static List<Path> searchJars(Path path) {
        if (path.toFile().exists()) {
            try (Stream<Path> walk = Files.walk(path)) {
                return walk.filter(p -> p.toString().toLowerCase().endsWith(".jar")).toList();
            } catch (IOException ignore) {
            }
        }
        return List.of();
    }

}
