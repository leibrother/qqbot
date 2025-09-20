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

    public void complete() {
        this.completed = true;
    }

    public BotApi getApi() {
        return bot.getApi();
    }

    public Events getEvent() {
        String event = payload.getEvent();
        return Events.valueOf(event);
    }

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

    public <T> T getService(Class<T> clazz) {
        return getService(clazz, null);
    }

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

    public void addReplyHook(BiConsumer<Message, MessageResponse> hook) {
        ObjectUtils._assert(hook, "hook must not be null");
        replyHooks.add(hook);
    }

    private void runReplyHooks(Message message, MessageResponse response) {
        synchronized (replyHooks) {
            for (BiConsumer<Message, MessageResponse> hook : replyHooks) {
                hook.accept(message, response);
            }
        }
    }

    public void reply(Message message) {
        Topic topic = getTopic();
        JsonNode data = getPayload().getData();
        String replyId = data.get("id").asText();
        message.reply(replyId, replySequence.incrementAndGet());
        MessageResponse response = getBot().sendMessage(topic, message);
        runReplyHooks(message, response);
    }

    public void reply(MessageMedia media) {
        media.srvDontSend();
        Topic topic = getTopic();
        MessageMediaResponse response = getBot().sendMessage(topic, media);
        Message message = Message.media(response);
        reply(message);
    }

}
