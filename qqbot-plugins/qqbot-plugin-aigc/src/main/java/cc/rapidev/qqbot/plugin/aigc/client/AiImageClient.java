package cc.rapidev.qqbot.plugin.aigc.client;

import cc.rapidev.qqbot.plugin.aigc.AiConfig;
import dev.langchain4j.data.image.Image;

import java.lang.reflect.Constructor;

/**
 * @author leibrother
 */
public interface AiImageClient {

    static AiImageClient create(AiConfig config) {
        String client = config.getImageClient();
        try {
            Class<?> clazz = Class.forName(client);
            if (!AiImageClient.class.isAssignableFrom(clazz)) {
                throw new UnsupportedOperationException(String.format("%s is not a subclass of %s", client, AiImageClient.class));
            }
            Constructor<?> constructor = clazz.getDeclaredConstructor(AiConfig.class);
            constructor.setAccessible(true);
            return (AiImageClient) constructor.newInstance(config);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    Image generate(String text);

}
