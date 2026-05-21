package cc.rapidev.qqbot.message;

import cc.rapidev.qqbot.Bot;
import cc.rapidev.qqbot.BotPayload;
import cc.rapidev.qqbot.api.model.Message;
import cc.rapidev.qqbot.common.Events;
import cc.rapidev.qqbot.common.interfaces.Disposable;
import cc.rapidev.qqbot.common.utils.ExceptionUtils;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 消息调度器，用于将不同类型的消息分配给不同的处理器执行
 *
 * @author leibrother
 */
public class MessageDispatcher implements Disposable {

    private final Logger log = LoggerFactory.getLogger("[Bot Message Dispatcher]");

    @Getter
    private final Bot bot;
    private final Map<String, List<MessageHandler>> messageHandlers = new Hashtable<>();
    private final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

    public MessageDispatcher(Bot bot) {
        this.bot = bot;
        this.init();
    }

    /**
     * 加载所有声明的{@link MessageHandlerInjector}并注入
     */
    private void init() {
        this.register("_default", new NotImplMessageHandler());
        List<MessageHandlerInjector> injectors = MessageHandlerInjectorLoader.load();
        injectors.forEach(this::register);
    }

    @Override
    public void destroy() {
        this.executor.shutdown();
        this.messageHandlers.clear();
    }

    /**
     * 注册消息处理器
     *
     * @param name    注册名称
     * @param handler 消息处理器
     */
    public void register(String name, MessageHandler handler) {
        List<MessageHandler> handlers = messageHandlers.computeIfAbsent(name, _ -> new ArrayList<>());
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

    /**
     * 注册消息处理器
     *
     * @param event    - 消息事件
     * @param handlers - 消息处理器列表
     */
    public void register(Events event, List<MessageHandler> handlers) {
        if (handlers != null && !handlers.isEmpty()) {
            handlers.forEach(handler -> register(event, handler));
        }
    }

    /**
     * 通过{@link MessageHandlerInjector}注入消息处理器
     *
     * @param injector 消息处理器注入器
     */
    public void register(MessageHandlerInjector injector) {
        if (injector == null) {
            throw new IllegalArgumentException("injector must not be null");
        }
        injector.inject(this);
    }

    /**
     * 获取传入参数对应的处理器列表
     *
     * @param name 事件名
     * @return 消息处理器列表
     */
    private List<MessageHandler> getHandlers(String name) {
        List<MessageHandler> handlers = messageHandlers.get(name);
        if (handlers == null || handlers.isEmpty()) {
            return messageHandlers.get("_default");
        }
        return handlers;
    }

    /**
     * 获取传入参数对应的处理器列表
     *
     * @param event 事件
     * @return 消息处理器列表
     */
    private List<MessageHandler> getHandlers(Events event) {
        return getHandlers(event.name());
    }

    /**
     * 获取消息上下文
     *
     * @param payload 消息内容
     * @return {@link MessageContext}
     */
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
        Events event = context.event();
        List<MessageHandler> handlers = getHandlers(event);
        Runnable runnable = () -> {
            Iterator<MessageHandler> iterator = handlers.iterator();
            while (iterator.hasNext()) {
                try {
                    MessageHandler handler = iterator.next();
                    if (context.isCompleted() && !handler.isRequired()) {
                        continue;
                    }
                    handler.handle(context);
                } catch (Exception e) {
                    log.error("message handler error", e);
                    if (context.topic().isPrivate()) {
                        Message message = generateStackTraceMessage(e);
                        context.reply(message);
                    }
                }
            }
        };
        executor.execute(runnable);
    }

    private Message generateStackTraceMessage(Exception e) {
        String trace = ExceptionUtils.getStackTrace(e);
        String template = """
                ### 机器人发生异常，请联系开发者
                
                ---
                
                异常堆栈
                ```java
                %s
                ```
                """;
        return Message.markdown(template.formatted(trace));
    }

}
