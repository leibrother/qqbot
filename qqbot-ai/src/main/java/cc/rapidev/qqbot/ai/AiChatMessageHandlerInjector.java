package cc.rapidev.qqbot.ai;

import cc.rapidev.qqbot.Bot;
import cc.rapidev.qqbot.ai.client.AiChatClient;
import cc.rapidev.qqbot.common.Events;
import cc.rapidev.qqbot.message.MessageDispatcher;
import cc.rapidev.qqbot.message.MessageHandlerInjector;

/**
 * @author leibrother
 */
public class AiChatMessageHandlerInjector implements MessageHandlerInjector {

    @Override
    public void inject(MessageDispatcher dispatcher) {
        Bot bot = dispatcher.getBot();
        AiConfig config = new AiConfig(bot.getConfig());
        if (!config.isEnable()) {
            return;
        }
        AiChatClient client = AiChatClient.create(config);
        AiChatMessageHandler handler = new AiChatMessageHandler(client);
        Events.messageCreateEvents.forEach(event -> dispatcher.register(event, handler));
    }

}
