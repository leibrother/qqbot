package cc.rapidev.qqbot.boot.plugin;

import cc.rapidev.qqbot.Bot;
import cc.rapidev.qqbot.common.Version;
import cc.rapidev.qqbot.message.MessageHandlerInjector;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.List;

/**
 * @author leibrother
 */
public class Plugin {

    private final Path jar;
    private final Manifest manifest;

    public Plugin(Path jar) throws IOException {
        this.jar = jar;
        this.manifest = manifest(jar);
    }

    private static Manifest manifest(Path jar) throws IOException {
        URL url = jar.toUri().toURL();
        try (URLClassLoader loader = new URLClassLoader(new URL[]{url})) {
            InputStream input = loader.getResourceAsStream("plugin.json");
            if (input == null) {
                throw new NoSuchFileException("jar '%s' not found plugin.json in resources".formatted(jar.toString()));
            }
            String data = new String(input.readAllBytes());
            return Manifest.parse(data);
        }
    }

    public Path jar() {
        return this.jar;
    }

    public String name() {
        return this.manifest.name();
    }

    public Version version() {
        return this.manifest.version();
    }

    public Manifest manifest() {
        return this.manifest;
    }

    public void enable(Bot bot) {
        try (URLClassLoader classLoader = new URLClassLoader(new URL[]{jar.toUri().toURL()})) {
            List<String> injectors = manifest.injectors();
            for (String injector : injectors) {
                Class<?> clazz = classLoader.loadClass(injector);
                Constructor<?> constructor = clazz.getDeclaredConstructor();
                constructor.setAccessible(true);
                MessageHandlerInjector instance = (MessageHandlerInjector) constructor.newInstance();
                bot.getDispatcher().register(instance);
            }
        } catch (IOException | ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

}
