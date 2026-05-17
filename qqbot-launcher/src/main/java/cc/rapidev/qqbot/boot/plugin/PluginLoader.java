package cc.rapidev.qqbot.boot.plugin;

import cc.rapidev.qqbot.common.utils.IdentityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;

/**
 * 插件加载器
 * <p>加载指定目录下声明了<code>plugin.json</code>文件的JAR包</p>
 *
 * @author leibrother
 */
public class PluginLoader {

    private final Logger logger = LoggerFactory.getLogger(PluginLoader.class);
    private final File tmpdir;
    private final List<Path> paths;
    private volatile List<Plugin> plugins;

    public PluginLoader(List<Path> paths) {
        this.tmpdir = new File(System.getProperty("java.io.tmpdir"), "qqbot-plugins@" + IdentityUtils.shortID());
        if (!tmpdir.mkdirs()) {
            throw new RuntimeException("creating a temp directory failed");
        }
        this.tmpdir.deleteOnExit();
        logger.debug("plugins temp directory is {}", tmpdir);
        this.paths = paths;
    }

    public List<Plugin> plugins() {
        if (this.plugins == null) {
            synchronized (this) {
                if (this.plugins == null) {
                    this.plugins = search();
                }
            }
        }
        return this.plugins;
    }

    /**
     * 获取指定路径下的插件列表
     *
     * @return 插件列表
     */
    private List<Plugin> search() {
        List<Plugin> list = new ArrayList<>();
        for (Path path : this.paths) {
            for (Path jarpath : searchJars(path)) {
                try {
                    Manifest manifest = manifest(jarpath);
                    Plugin plugin = copyJarAsPlugin(manifest, new File(jarpath.toUri()));
                    list.add(plugin);
                } catch (Exception ignore) {
                }
            }
        }
        return list;
    }

    /**
     * 搜索JAR
     *
     * @param path 文件夹或文件路径
     * @return 所有的JAR路径
     */
    private List<Path> searchJars(Path path) {
        if (path.toFile().exists()) {
            try (Stream<Path> walk = Files.walk(path)) {
                return walk.filter(p -> p.toString().toLowerCase().endsWith(".jar")).toList();
            } catch (IOException ignore) {
            }
        }
        return List.of();
    }

    /**
     * 解析一个JAR文件下的plugin.json
     *
     * @param jarpath JAR路径
     * @return Manifest
     * @throws IOException JAR不存在plugin.json会产生IOException
     */
    private Manifest manifest(Path jarpath) throws IOException {
        try (URLClassLoader loader = new URLClassLoader(new URL[]{jarpath.toUri().toURL()})) {
            InputStream input = loader.getResourceAsStream("plugin.json");
            if (input == null) {
                throw new NoSuchFileException("jar '%s' not found plugin.json in resources".formatted(jarpath.toString()));
            }
            String data = new String(input.readAllBytes());
            return Manifest.parse(data);
        }
    }

    /**
     * 拷贝JAR并转为Plugin对象
     *
     * @param manifest 插件声明文件
     * @param jar      JAR
     * @return Plugin
     * @throws IOException 拷贝失败会产生IOException
     */
    private Plugin copyJarAsPlugin(Manifest manifest, File jar) throws IOException {
        String name = manifest.name() + "@" + manifest.version();
        File target = new File(this.tmpdir, name);
        if (!target.mkdir()) {
            throw new IOException("create temp directory '%s' failed".formatted(target.getPath()));
        }
        File pluginJar = new File(target, "plugin.jar");
        Files.copy(new FileInputStream(jar), pluginJar.toPath());
        File pluginJarLibDir = new File(target, "lib");
        if (!pluginJarLibDir.mkdir()) {
            target.deleteOnExit();
            throw new IOException("create temp directory '%s' failed".formatted(pluginJarLibDir.getPath()));
        }
        List<File> pluginJarLibs = extractLibs(pluginJar, pluginJarLibDir);
        return new Plugin(manifest, pluginJar, pluginJarLibs);
    }

    /**
     * 将JAR内部/lib目录下的JAR文件全部拷贝到指定文件夹
     *
     * @param jar JAR
     * @param dir 拷贝到此文件夹
     * @return 拷贝完成的文件列表
     * @throws IOException 读取JAR失败或者拷贝失败会产生IOException
     */
    private List<File> extractLibs(File jar, File dir) throws IOException {
        List<File> libs = new ArrayList<>();
        try (JarFile jarFile = new JarFile(jar)) {
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry element = entries.nextElement();
                if (element.getName().startsWith("lib/") && element.getName().endsWith(".jar")) {
                    File file = new File(dir, new File(element.getName()).getName());
                    Files.copy(jarFile.getInputStream(element), file.toPath());
                    libs.add(file);
                }
            }
        }
        return libs;
    }

}
