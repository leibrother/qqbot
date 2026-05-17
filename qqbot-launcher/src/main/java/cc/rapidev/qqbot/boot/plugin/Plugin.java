package cc.rapidev.qqbot.boot.plugin;

import cc.rapidev.qqbot.common.Version;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author leibrother
 */
public class Plugin {

    private final Manifest manifest;
    private final File pluginJar;
    private final List<File> pluginLibs;
    private ClassLoader classLoader;

    public Plugin(Manifest manifest, File pluginJar, List<File> pluginLibs) {
        this.manifest = manifest;
        this.pluginJar = pluginJar;
        this.pluginLibs = pluginLibs;
    }

    public Manifest manifest() {
        return this.manifest;
    }

    public String name() {
        return this.manifest.name();
    }

    public Version version() {
        return this.manifest.version();
    }

    public URL[] urls() {
        return Stream.concat(Stream.of(this.pluginJar), this.pluginLibs.stream())
                .map(file -> {
                    try {
                        return file.toURI().toURL();
                    } catch (MalformedURLException e) {
                        throw new RuntimeException(e);
                    }
                }).toArray(URL[]::new);
    }

}
