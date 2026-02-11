package cc.rapidev.qqbot.message.memory;

import cc.rapidev.qqbot.common.Events;
import cc.rapidev.qqbot.common.Topic;
import cc.rapidev.qqbot.common.interfaces.Converter;
import cc.rapidev.qqbot.message.MessageContext;
import cc.rapidev.qqbot.message.MessageHandler;
import cc.rapidev.qqbot.message.memory.repository.InMemoryMessageRepository;
import cc.rapidev.qqbot.message.memory.repository.MessageRepository;

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
public class MemoryHandler implements MessageHandler {

    private final Converter<MessageContext, MemoryMessage> converter;
    private final MessageRepository repository;

    public MemoryHandler() {
        this.converter = new MessageConverter();
        this.repository = new InMemoryMessageRepository();
    }

    public MemoryHandler(MessageRepository messageRepository) {
        this.converter = new MessageConverter();
        this.repository = messageRepository;
    }

    @Override
    public int order() {
        return 0;
    }

    @Override
    public boolean isRequired() {
        return true;
    }

    @Override
    public void handle(MessageContext context) {
        Events event = context.getEvent();
        if (event.isMessageCreate()) {
            Topic topic = context.getTopic();
            MemoryMessage message = converter.convert(context);
            MemoryService service = new MemoryService(repository, topic, message);
            context.addService("memoryService", service);
            // 注册回复钩子
            registerReplyHook(context);
        }
    }

    private void registerReplyHook(MessageContext context) {
        context.addReplyHook((message, response) -> {
            if (!message.isText()) {
                return;
            }
            MemoryMessage memoryMessage = MemoryMessage.builder()
                    .id(response.getId())
                    .bot(true)
                    .text(message.getContent())
                    .timestamp(response.getTime())
                    .build();
            MemoryService service = context.getService(MemoryService.class);
            service.add(memoryMessage);
        });
    }

}
