package cc.rapidev.qqbot.plugin.aigc.client;

import cc.rapidev.qqbot.memory.model.MemoryMessage;
import cc.rapidev.qqbot.message.MessageContext;
import cc.rapidev.qqbot.plugin.aigc.AiConfig;
import cc.rapidev.qqbot.plugin.aigc.tools.AiTool;

import java.lang.reflect.Constructor;
import java.util.List;

/**
 * @author leibrother
 */
public interface AiChatClient {

    static AiChatClient create(AiConfig config) {
        String client = config.getChatClient();
        try {
            Class<?> clazz = Class.forName(client);
            if (!AiChatClient.class.isAssignableFrom(clazz)) {
                throw new UnsupportedOperationException(String.format("%s is not a subclass of %s", client, AiChatClient.class));
            }
            Constructor<?> constructor = clazz.getDeclaredConstructor(AiConfig.class);
            constructor.setAccessible(true);
            return (AiChatClient) constructor.newInstance(config);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    void addTool(AiTool tool);

    String chat(MessageContext context, List<MemoryMessage> messages);

}
