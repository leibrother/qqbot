package cc.rapidev.qqbot.boot.plugin.cl;

import java.net.URL;
import java.util.Arrays;

/**
 * @author leibrother
 */
public class CompositeClassLoader extends ClassLoader {

    static {
        registerAsParallelCapable();
    }

    private final Iterable<? extends ClassLoader> delegates;

    public CompositeClassLoader(ClassLoader... delegates) {
        this(Arrays.asList(delegates));
    }

    public CompositeClassLoader(Iterable<? extends ClassLoader> delegates) {
        super(null);
        this.delegates = delegates;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        for (ClassLoader delegate : delegates) {
            try {
                return delegate.loadClass(name);
            } catch (ClassNotFoundException ignored) {
            }
        }
        throw new ClassNotFoundException(name);
    }

    @Override
    protected URL findResource(String name) {
        for (ClassLoader delegate : delegates) {
            URL url = delegate.getResource(name);
            if (url != null) return url;
        }
        return null;
    }

}
