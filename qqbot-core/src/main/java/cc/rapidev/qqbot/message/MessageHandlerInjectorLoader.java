package cc.rapidev.qqbot.message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>{@link MessageHandlerInjector}加载器</p>
 * <p>用于加载{@code qqbot.injectors}中声明的{@link MessageHandlerInjector}</p>
 * <p>{@code qqbot.injectors}文件的内容为每行一个{@link MessageHandlerInjector}实现类，该类需拥有默认构造器</p>
 *
 * @author leibrother
 */
public final class MessageHandlerInjectorLoader {

    private static final String resource_file_name = "META-INF/qqbot.injectors";

    private static Set<String> readResourceAsClassNames() {
        Set<String> classNames = new HashSet<>();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try {
            Enumeration<URL> urls = classLoader.getResources(resource_file_name);
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                reader.lines()
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .forEach(classNames::add);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return classNames;
    }

    private static MessageHandlerInjector loadClass(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            if (!MessageHandlerInjector.class.isAssignableFrom(clazz)) {
                throw new ClassCastException("%s cannot be cast to %s".formatted(className, MessageHandlerInjector.class.getName()));
            }
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return (MessageHandlerInjector) constructor.newInstance();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<MessageHandlerInjector> load() {
        return readResourceAsClassNames().stream()
                .map(MessageHandlerInjectorLoader::loadClass)
                .toList();
    }

}
