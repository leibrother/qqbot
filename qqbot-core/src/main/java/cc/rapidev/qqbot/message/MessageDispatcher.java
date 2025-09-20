package cc.rapidev.qqbot.message;

import cc.rapidev.qqbot.Bot;
import cc.rapidev.qqbot.BotPayload;
import cc.rapidev.qqbot.common.Events;
import cc.rapidev.qqbot.exception.BotException;
import cc.rapidev.qqbot.message.handler.BotStartedHandler;
import cc.rapidev.qqbot.message.memory.MemoryMessageHandler;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 消息调度器，用于将不同类型的消息分配给不同的执行器执行
 *
 * @author leibrother
 */
public class MessageDispatcher {

    private final Logger log = LoggerFactory.getLogger("[Bot Message Dispatcher]");

    @Getter
    private final Bot bot;
    private final Map<String, List<MessageHandler>> messageHandlers = new Hashtable<>();
    private final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

    public MessageDispatcher(Bot bot) {
        this.bot = bot;
        this.init();
    }

    private void init() {
        this.register("_default", new NotImplMessageHandler());
        this.register(new BotStartedHandler());
        this.register(new MemoryMessageHandler());
        List<MessageHandlerInjector> injectors = MessageHandlerInjectorLoader.load();
        injectors.forEach(this::register);
    }

    /**
     * 注册消息处理器
     *
     * @param name    注册名称
     * @param handler 消息处理器
     */
    public void register(String name, MessageHandler handler) {
        List<MessageHandler> handlers = messageHandlers.computeIfAbsent(name, k -> new ArrayList<>());
        handlers.add(handler);
        handlers.sort(Comparable::compareTo);
    }

    /**
     * 注册消息处理器
     *
     * @param event   消息事件
     * @param handler 消息处理器
     */
    public void register(Events event, MessageHandler handler) {
        register(event.name(), handler);
    }

    public void register(MessageHandlerInjector injector) {
        if (injector == null) {
            throw new IllegalArgumentException("injector must not be null");
        }
        injector.inject(this);
    }

    private List<MessageHandler> getHandlers(String name) {
        List<MessageHandler> handlers = messageHandlers.get(name);
        if (handlers == null || handlers.isEmpty()) {
            return messageHandlers.get("_default");
        }
        return handlers;
    }

    private List<MessageHandler> getHandlers(Events event) {
        return getHandlers(event.name());
    }

    private MessageContext getContext(BotPayload payload) {
        return new MessageContext(bot, payload);
    }

    /**
     * 派遣
     * <p>将{@link BotPayload}包装为{@link MessageContext}然后投入线程池，由{@link MessageHandler}处理</p>
     *
     * @param payload 载荷
     */
    public void doDispatch(BotPayload payload) {
        MessageContext context = getContext(payload);
        Events event = context.getEvent();
        List<MessageHandler> handlers = getHandlers(event);
        Runnable runnable = () -> {
            try {
                for (MessageHandler handler : handlers) {
                    if (context.isCompleted() && !handler.must()) {
                        continue;
                    }
                    handler.handle(context);
                }
            } catch (BotException e) {
                log.error("message handler error", e);
            }
        };
        executor.execute(runnable);
    }

}
