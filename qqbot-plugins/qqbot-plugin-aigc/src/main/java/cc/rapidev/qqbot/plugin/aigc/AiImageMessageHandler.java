package cc.rapidev.qqbot.plugin.aigc;

import cc.rapidev.qqbot.message.MessageContext;
import cc.rapidev.qqbot.message.MessageHandler;
import cc.rapidev.qqbot.plugin.aigc.client.AiImageClient;

import java.util.List;

/**
 * @author leibrother
 */
public class AiImageMessageHandler implements MessageHandler {

    private final AiImageClient client;
    private final List<String> keywords;

    public AiImageMessageHandler(AiImageClient client, List<String> keywords) {
        this.client = client;
        this.keywords = keywords;
    }

    @Override
    public void handle(MessageContext context) {
//        MatcherService matcher = context.getService(MatcherService.class);
//        if (!matcher.branch().match(keywords)) {
//            return;
//        }
//        context.complete();
//        String prompt = matcher.current();
//        Image image = client.generate(prompt);
//        context.reply(MessageMedia.image(image.url().toString()));
//        // 遗忘
//        context.getService(MemoryService.class).forget();
    }

}
