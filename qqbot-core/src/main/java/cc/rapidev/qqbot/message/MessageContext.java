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
import cc.rapidev.qqbot.common.service.ServiceRegistrationCenter;
import cc.rapidev.qqbot.common.utils.Asserts;
import cc.rapidev.qqbot.message.converter.MessageConverter;
import cc.rapidev.qqbot.message.converter.TopicConverter;
import cc.rapidev.qqbot.message.model.MessageGeneric;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

/**
 * @author leibrother
 */
public final class MessageContext extends ServiceRegistrationCenter {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Getter
    private final Bot bot;
    private final BotPayload payload;
    private final AtomicInteger replySequence = new AtomicInteger(0);
    private final List<BiConsumer<Message, MessageResponse>> replyHooks = new ArrayList<>();
    private final Topic topic;
    private final MessageGeneric message;
    @Getter
    private volatile boolean completed = false;

    public MessageContext(Bot bot, BotPayload payload) {
        this.bot = bot;
        this.payload = payload;
        this.topic = TopicConverter.INSTANCE.convert(payload);
        this.message = MessageConverter.INSTANCE.convert(payload);
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
    public BotApi api() {
        return bot.api();
    }

    /**
     * 获取当前消息事件类型
     *
     * @return {@link Events}
     */
    public Events event() {
        return Events.valueOf(payload.event());
    }

    /**
     * 获取有效载荷
     *
     * @return payload
     */
    public BotPayload payload() {
        return this.payload;
    }

    /**
     * 获取当前消息的Topic
     *
     * @return {@link Topic}
     */
    public Topic topic() {
        return this.topic;
    }


    /**
     * 获取上下文中的消息
     *
     * @return {@link MessageGeneric}
     */
    public MessageGeneric message() {
        return this.message;
    }

    /**
     * 添加回复消息钩子，此钩子会在调用{@link MessageContext#reply(Message)}时触发
     *
     * @param hook 钩子
     */
    public void addReplyHook(BiConsumer<Message, MessageResponse> hook) {
        Asserts.notnull(hook, "hook must not be null");
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
        Topic topic = topic();
        JsonNode data = payload().data();
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
        Topic topic = topic();
        MessageMediaResponse response = getBot().sendMessage(topic, media);
        response.setFileType(media.getFileType());
        Message message = Message.media(response);
        reply(message);
    }

}
