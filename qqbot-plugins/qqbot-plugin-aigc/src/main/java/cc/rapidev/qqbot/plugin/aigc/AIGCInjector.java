package cc.rapidev.qqbot.plugin.aigc;

import cc.rapidev.qqbot.Bot;
import cc.rapidev.qqbot.common.Events;
import cc.rapidev.qqbot.message.MessageDispatcher;
import cc.rapidev.qqbot.message.MessageHandlerInjector;
import cc.rapidev.qqbot.plugin.aigc.client.AiChatClient;
import cc.rapidev.qqbot.plugin.aigc.client.AiImageClient;
import cc.rapidev.qqbot.plugin.aigc.tools.AiImageTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author leibrother
 */
public class AIGCInjector implements MessageHandlerInjector {

    private final Logger logger = LoggerFactory.getLogger("AIGC");

    @Override
    public void inject(MessageDispatcher dispatcher) {
        Bot bot = dispatcher.getBot();
        AiConfig config = new AiConfig(bot.getConfig());
        AiChatClient aiChatClient = null;
        // 注入AI聊天处理器
        if (config.isChatEnable()) {
            aiChatClient = AiChatClient.create(config);
            AiChatMessageHandler handler = new AiChatMessageHandler(aiChatClient);
            Events.messageCreateEvents.forEach(event -> dispatcher.register(event, handler));
        }
        // 注入AI生图处理器
        AiImageClient aiImageClient = null;
        if (config.isImageEnable()) {
            aiImageClient = AiImageClient.create(config);
            List<String> keywords = config.getImageKeywords();
            AiImageMessageHandler handler = new AiImageMessageHandler(aiImageClient, keywords);
            Events.messageCreateEvents.forEach(event -> dispatcher.register(event, handler));
        }
        // 如果同时开启聊天与生图，则向聊天客户端注入生图工具
        if (config.isChatEnable() && config.isImageEnable()) {
            assert aiChatClient != null;
            assert aiImageClient != null;
            AiImageTool tool = new AiImageTool(aiImageClient);
            aiChatClient.addTool(tool);
        }

        logger.info("已启用AIGC插件");
    }

}
