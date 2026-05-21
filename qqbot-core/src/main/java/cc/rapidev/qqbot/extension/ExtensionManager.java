package cc.rapidev.qqbot.extension;

import cc.rapidev.qqbot.Bot;
import cc.rapidev.qqbot.common.interfaces.Disposable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @author leibrother
 */
public class ExtensionManager implements Disposable {

    private final Bot bot;
    private final List<Extension> extensions = new ArrayList<>();
    private volatile boolean initialized = false;

    public ExtensionManager(Bot bot) {
        this.bot = bot;
    }

    public List<Class<? extends Extension>> declared() {
        return ExtensionDeclarer.declared();
    }

    /**
     * 初始化扩展管理器
     * <p>将所有声明的扩展实例化</p>
     */
    public void init() {
        if (!initialized) {
            synchronized (this) {
                if (!initialized) {
                    for (Class<? extends Extension> clazz : declared()) {
                        Extension extension = this.initExtension(clazz);
                        this.extensions.add(extension);
                    }
                    this.initialized = true;
                }
            }
        }
    }

    /**
     * 关闭扩展管理器
     * <p>关闭所有实例化的扩展</p>
     */
    public synchronized void destroy() {
        if (initialized) {
            for (Extension extension : this.extensions) {
                try {
                    extension.destroy();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            this.extensions.clear();
            this.initialized = false;
        }
    }

    private Extension initExtension(Class<? extends Extension> clazz) {
        List<Constructor<?>> constructors = Arrays.stream(clazz.getDeclaredConstructors())
                .filter(constructor -> {
                    constructor.setAccessible(true);
                    Class<?>[] types = constructor.getParameterTypes();
                    if (types.length == 0) {
                        return true;
                    } else if (types.length == 1) {
                        return types[0].equals(Bot.class);
                    } else {
                        return false;
                    }
                })
                .sorted(Comparator.comparing(Constructor::getParameterCount, Comparator.reverseOrder()))
                .toList();

        if (constructors.isEmpty()) {
            throw new RuntimeException("%s 没有可用的构造器".formatted(clazz));
        }
        Constructor<?> constructor = constructors.getFirst();
        try {
            if (constructor.getParameterCount() == 1) {
                return (Extension) constructor.newInstance(this.bot);
            } else {
                return (Extension) constructor.newInstance();
            }
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
