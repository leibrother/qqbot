package cc.rapidev.qqbot.extension;

import cc.rapidev.qqbot.Bot;
import cc.rapidev.qqbot.common.interfaces.Disposable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author leibrother
 */
public class ExtensionManager implements Disposable {

    private final Logger logger = LoggerFactory.getLogger(ExtensionManager.class);
    private final Bot bot;
    private final List<Class<? extends Extension>> declared = new ArrayList<>();
    private final List<Extension> extensions = new ArrayList<>();
    private volatile boolean initialized = false;

    public ExtensionManager(Bot bot) {
        this.bot = bot;
    }

    public List<Class<? extends Extension>> declared() {
        return Stream.concat(ExtensionDeclarer.declared().stream(), declared.stream()).toList();
    }

    public synchronized void declare(Class<? extends Extension> clazz) {
        if (declared().contains(clazz)) {
            return;
        }
        this.declared.add(clazz);
        if (this.initialized) {
            this.initExtension(clazz);
        }
    }

    /**
     * 初始化扩展管理器
     * <p>将所有声明的扩展实例化</p>
     */
    public void init() {
        synchronized (this) {
            if (!initialized) {
                this.initialized = true;
                for (Class<? extends Extension> clazz : declared()) {
                    try {
                        Extension extension = this.initExtension(clazz);
                        this.extensions.add(extension);
                    } catch (RuntimeException e) {
                        logger.error("init extension error:", e);
                    }
                }
            }
        }
    }

    private Extension initExtension(Class<? extends Extension> clazz) {
        try {
            Constructor<? extends Extension> constructor = clazz.getDeclaredConstructor();
            Extension extension = constructor.newInstance();
            extension.ready(bot);
            return extension;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 关闭扩展管理器
     * <p>关闭所有实例化的扩展</p>
     */
    public void destroy() {
        synchronized (this) {
            if (initialized) {
                this.initialized = false;
                for (Extension extension : this.extensions.reversed()) {
                    try {
                        extension.destroy();
                    } catch (Exception e) {
                        logger.error("destroy extension error", e);
                    }
                }
                this.extensions.clear();
            }
        }
    }

}
