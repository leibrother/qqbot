package cc.rapidev.qqbot.launcher.plugin.cl;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author leibrother
 */
public class PluginClassLoader extends URLClassLoader {

    static {
        registerAsParallelCapable();
    }

    public PluginClassLoader(URL[] urls) {
        super(urls, null);
    }

    public PluginClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

}
