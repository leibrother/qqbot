package cc.rapidev.qqbot.ai;

import cc.rapidev.qqbot.Bot;
import cc.rapidev.qqbot.ai.client.AiImageClient;
import cc.rapidev.qqbot.common.Events;
import cc.rapidev.qqbot.message.MessageDispatcher;
import cc.rapidev.qqbot.message.MessageHandlerInjector;

import java.util.List;

/**
 * @author leibrother
 */
public class AiImageMessageHandlerInjector implements MessageHandlerInjector {

    @Override
    public void inject(MessageDispatcher dispatcher) {
        Bot bot = dispatcher.getBot();
        AiConfig config = new AiConfig(bot.getConfig());
        if (!config.isImageEnable()) {
            return;
        }
        AiImageClient client = AiImageClient.create(config);
        List<String> keywords = config.getImageKeywords();
        AiImageMessageHandler handler = new AiImageMessageHandler(client, keywords);
        Events.messageCreateEvents.forEach(event -> dispatcher.register(event, handler));
    }

}
