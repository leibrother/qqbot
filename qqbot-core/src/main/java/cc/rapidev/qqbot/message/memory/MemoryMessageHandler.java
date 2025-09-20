package cc.rapidev.qqbot.message.memory;

import cc.rapidev.qqbot.common.Events;
import cc.rapidev.qqbot.common.interfaces.Converter;
import cc.rapidev.qqbot.message.MessageContext;
import cc.rapidev.qqbot.message.MessageDispatcher;
import cc.rapidev.qqbot.message.MessageHandler;
import cc.rapidev.qqbot.message.MessageHandlerInjector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 记忆消息处理
 * <p>Features</p>
 * <ul>
 *     <li>解析{@link cc.rapidev.qqbot.BotPayload payload}中携带的消息为{@link MemoryMessage}</li>
 *     <li>通过{@link MessageRepository}存储消息</li>
 *     <li>注册{@link MemoryService}服务到{@link MessageContext}，通过{@link MessageContext#getService}获取此服务</li>
 * </ul>
 *
 * @author leibrother
 */
public class MemoryMessageHandler implements MessageHandler, MessageHandlerInjector {

    private final Logger logger = LoggerFactory.getLogger(MemoryMessageHandler.class);

    private final Converter<MessageContext, MemoryMessage> converter;
    private final MessageRepository repository;

    public MemoryMessageHandler() {
        this.converter = new MessageConverter();
        this.repository = new InMemoryMessageRepository();
    }

    public MemoryMessageHandler(MessageRepository messageRepository) {
        this.converter = new MessageConverter();
        this.repository = messageRepository;
    }

    @Override
    public int order() {
        return 0;
    }

    @Override
    public boolean must() {
        return true;
    }

    @Override
    public void inject(MessageDispatcher dispatcher) {
        logger.info("inject message handler: {}", this.getClass().getName());
        Events.messageCreateEvents.forEach(event -> dispatcher.register(event, this));
        //现在撤回消息好像没有回调了
        //Events.messageDeleteEvents.forEach(event -> dispatcher.register(event, this));
    }

    @Override
    public void handle(MessageContext context) {
        Events event = context.getEvent();
        if (event.isMessageCreate()) {
            MemoryMessage message = converter.convert(context);
            MemoryService service = new MemoryService(repository, context.getTopic(), message);
            service.remember();
            context.addService("memoryService", service);
        }
    }

}
