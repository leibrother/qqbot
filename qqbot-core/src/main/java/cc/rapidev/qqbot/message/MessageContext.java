package cc.rapidev.qqbot.message;

import cc.rapidev.qqbot.Bot;
import cc.rapidev.qqbot.BotPayload;
import cc.rapidev.qqbot.api.BotApi;
import cc.rapidev.qqbot.api.model.Message;
import cc.rapidev.qqbot.api.model.MessageMedia;
import cc.rapidev.qqbot.api.response.MessageMediaResponse;
import cc.rapidev.qqbot.api.response.MessageResponse;
import cc.rapidev.qqbot.common.Events;
import cc.rapidev.qqbot.common.Topic;
import cc.rapidev.qqbot.common.utils.ObjectUtils;
import cc.rapidev.qqbot.exception.BotException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

/**
 * @author leibrother
 */
public final class MessageContext {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Getter
    private final Bot bot;
    @Getter
    private final BotPayload payload;
    private final Map<String, Object> services = new HashMap<>();
    private final Map<Class<?>, List<String>> servicesNames = new HashMap<>();
    private final AtomicInteger replySequence = new AtomicInteger(0);
    private final List<BiConsumer<Message, MessageResponse>> replyHooks = new ArrayList<>();
    private volatile Topic topic;
    @Getter
    private volatile boolean completed = false;

    public MessageContext(Bot bot, BotPayload payload) {
        this.bot = bot;
        this.payload = payload;
    }

    /**
     * 将上下文标记为完成状态
     * <p>当上下文被标记为完成后，非必要的{@link MessageHandler}将会跳过执行</p>
     */
    public void complete() {
        this.completed = true;
    }

    /**
     * 获取机器人Api服务
     * <p>可通过它与调用机器人接口</p>
     *
     * @return {@link BotApi}
     */
    public BotApi getApi() {
        return bot.getApi();
    }

    /**
     * 获取当前消息事件类型
     *
     * @return {@link Events}
     */
    public Events getEvent() {
        String event = payload.getEvent();
        return Events.valueOf(event);
    }

    /**
     * 添加一个服务，后续可通过类与名称获取它
     *
     * @param name    服务名
     * @param service 服务实例
     */
    public void addService(String name, Object service) {
        ObjectUtils._assert(name, "service name must not be null");
        ObjectUtils._assert(service, "service object must not be null");
        if (this.services.containsKey(name)) {
            throw new BotException("service %s already exists".formatted(name));
        }
        Class<?> clazz = service.getClass();
        this.services.put(name, service);
        this.servicesNames.computeIfAbsent(clazz, k -> new ArrayList<>()).add(name);
    }

    /**
     * 通过类获取一个服务
     *
     * @param clazz 服务类
     * @param <T>   类型
     * @return 指定类型的服务实例
     */
    public <T> T getService(Class<T> clazz) {
        return getService(clazz, null);
    }

    /**
     * 通过类与服务名获取一个服务
     *
     * @param clazz 服务类
     * @param name  服务名
     * @param <T>   类型
     * @return 指定类型的服务实例
     */
    public <T> T getService(Class<T> clazz, String name) {
        ObjectUtils._assert(clazz, "service clazz must not be null");
        List<String> names = servicesNames.get(clazz);
        if (names == null || names.isEmpty()) {
            throw new BotException("service %s not exists".formatted(clazz.getName()));
        }
        if (name == null) {
            if (names.size() == 1) {
                return clazz.cast(services.get(names.getFirst()));
            } else {
                throw new BotException("service %s is are multiple, please specify a name".formatted(clazz.getName()));
            }
        } else {
            if (names.contains(name)) {
                return clazz.cast(services.get(name));
            } else {
                throw new BotException("service %s(%s) not exists".formatted(clazz.getName(), name));
            }
        }
    }

    /**
     * 获取当前消息的Topic
     *
     * @return {@link Topic}
     */
    public Topic getTopic() {
        if (topic == null) {
            synchronized (this) {
                if (topic == null) {
                    topic = generateTopic();
                    logger.debug("topic: {}", topic);
                }
            }
        }
        return topic;
    }

    /**
     * 根据消息类型构建Topic
     *
     * @return {@link Topic}
     */
    private Topic generateTopic() {
        Events event = getEvent();
        JsonNode data = payload.getData();
        switch (event) {
            case C2C_MESSAGE_CREATE -> {
                String id = data.path("author").get("id").asText();
                return Topic.ofPrivate(id);
            }
            case GROUP_AT_MESSAGE_CREATE -> {
                String id = data.get("group_id").asText();
                return Topic.ofGroupAt(id);
            }
            case DIRECT_MESSAGE_CREATE -> {
                String id = data.get("guild_id").asText();
                return Topic.ofDirect(id);
            }
            case MESSAGE_CREATE -> {
                String id = data.get("channel_id").asText();
                return Topic.ofGuild(id);
            }
            case AT_MESSAGE_CREATE -> {
                String id = data.get("channel_id").asText();
                return Topic.ofGuildAt(id);
            }
            default -> throw new BotException("current event %s unable to retrieve topic".formatted(event));
        }
    }

    /**
     * 添加回复消息钩子，此钩子会在调用{@link MessageContext#reply(Message)}时触发
     *
     * @param hook 钩子
     */
    public void addReplyHook(BiConsumer<Message, MessageResponse> hook) {
        ObjectUtils._assert(hook, "hook must not be null");
        replyHooks.add(hook);
    }

    /**
     * 运行回复消息钩子
     *
     * @param message  回复的消息
     * @param response 回复的结果
     */
    private void runReplyHooks(Message message, MessageResponse response) {
        synchronized (replyHooks) {
            for (BiConsumer<Message, MessageResponse> hook : replyHooks) {
                hook.accept(message, response);
            }
        }
    }

    /**
     * 回复消息到Topic
     *
     * @param message 消息内容
     */
    public void reply(Message message) {
        Topic topic = getTopic();
        JsonNode data = getPayload().getData();
        String replyId = data.get("id").asText();
        message.reply(replyId, replySequence.incrementAndGet());
        MessageResponse response = getBot().sendMessage(topic, message);
        runReplyHooks(message, response);
    }

    /**
     * 回复媒体消息到Topic
     *
     * @param media 媒体消息
     */
    public void reply(MessageMedia media) {
        media.srvDontSend();
        Topic topic = getTopic();
        MessageMediaResponse response = getBot().sendMessage(topic, media);
        Message message = Message.media(response);
        reply(message);
    }

}
