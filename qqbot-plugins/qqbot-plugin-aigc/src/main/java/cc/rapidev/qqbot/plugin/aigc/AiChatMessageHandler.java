package cc.rapidev.qqbot.plugin.aigc;

import cc.rapidev.qqbot.api.model.Message;
import cc.rapidev.qqbot.api.model.User;
import cc.rapidev.qqbot.common.Topic;
import cc.rapidev.qqbot.memory.model.MemoryMessage;
import cc.rapidev.qqbot.memory.service.MemoryService;
import cc.rapidev.qqbot.message.MessageContext;
import cc.rapidev.qqbot.message.MessageHandler;
import cc.rapidev.qqbot.plugin.aigc.client.AiChatClient;

import java.util.ArrayList;
import java.util.List;

/**
 * @author leibrother
 */
public class AiChatMessageHandler implements MessageHandler {

    private final AiChatClient client;
    private final List<String> thinkingLock = new ArrayList<>();

    public AiChatMessageHandler(AiChatClient client) {
        this.client = client;
    }

    @Override
    public int order() {
        // 尽量放在后面执行，因为此处理器来者不拒
        return Integer.MAX_VALUE - 1000;
    }

    @Override
    public void handle(MessageContext context) {
        context.complete();
        if (!getLock(context)) {
            return;
        }
        try {
            MemoryService service = context.use(MemoryService.class);
            List<MemoryMessage> memory = service.memory();
            String result = client.chat(context, memory);
            context.reply(Message.markdown(result));
        } finally {
            releaseLock(context);
        }
    }

    private boolean getLock(MessageContext context) {
        Topic topic = context.topic();
        if (this.thinkingLock.contains(topic.toString())) {
            User info = context.getBot().getInfo();
            String name = info.getCleanUsername();
            context.reply(Message.text("%s正在思考中...".formatted(name)));
            context.use(MemoryService.class).forget();
            return false;
        }
        return this.thinkingLock.add(topic.toString());
    }

    private void releaseLock(MessageContext context) {
        Topic topic = context.topic();
        this.thinkingLock.remove(topic.toString());
    }

}
