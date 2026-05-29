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
import cc.rapidev.qqbot.database.repository.user.UserEntity;
import cc.rapidev.qqbot.message.converter.AuthorConverter;
import cc.rapidev.qqbot.message.converter.MessageConverter;
import cc.rapidev.qqbot.message.converter.TopicConverter;
import cc.rapidev.qqbot.message.model.Author;
import cc.rapidev.qqbot.message.model.MessageGeneric;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

/**
 * @author leibrother
 */
public final class MessageContext extends ServiceRegistrationCenter {

    private final Bot bot;
    private final BotPayload payload;
    private final Topic topic;
    private final Author author;
    private final MessageGeneric message;
    private final AtomicInteger replySequence = new AtomicInteger(0);
    private final List<BiConsumer<Message, MessageResponse>> replyHooks = new ArrayList<>();
    @Getter
    private volatile boolean completed = false;

    public MessageContext(Bot bot, BotPayload payload) {
        this.bot = bot;
        this.payload = payload;
        this.topic = TopicConverter.INSTANCE.convert(payload);
        this.author = AuthorConverter.INSTANCE.convert(payload);
        this.message = MessageConverter.INSTANCE.convert(payload);
        if (this.author != null) {
            UserEntity entity = UserEntity.from(this.author);
            this.bot.database().users().store(entity);
        }
    }

    /**
     * 将上下文标记为完成状态
     * <p>当上下文被标记为完成后，非必要的{@link MessageHandler}将会跳过执行</p>
     */
    public void complete() {
        this.completed = true;
    }

    /**
     * 获取绑定的机器人
     *
     * @return 机器人
     */
    public Bot bot() {
        return this.bot;
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
        return payload.e();
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
     * 获取消息作者
     *
     * @return 消息作者
     */
    public Author author() {
        return this.author;
    }

    /**
     * 获取用户会话ID （User Session Identifier）
     *
     * @return 用户会话ID
     */
    public Optional<String> usid() {
        if (topic == null || author == null) return Optional.empty();
        if (topic.isPrivate()) {
            return Optional.of(topic.id());
        } else {
            return Optional.of(topic.id() + ":" + author.openid());
        }
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
     * 获取服务
     * <p>优先在本上下文查找，如果找不到则在机器人中查找</p>
     *
     * @param clazz 服务类
     * @return 服务实例
     */
    @Override
    public <T> Optional<T> get(Class<T> clazz) {
        Optional<T> optional = super.get(clazz);
        if (optional.isEmpty()) {
            return bot().get(clazz);
        }
        return optional;
    }

    /**
     * 使用服务
     * <p>优先在本上下文查找，找不到则在机器人中查找</p>
     *
     * @param clazz 服务类
     * @return 服务实例
     */
    @Override
    public <T> T use(Class<T> clazz) {
        Optional<T> optional = super.get(clazz);
        return optional.orElseGet(() -> bot().use(clazz));
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
        MessageResponse response = bot().sendMessage(topic, message);
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
        MessageMediaResponse response = bot().sendMessage(topic, media);
        response.setFileType(media.getFileType());
        Message message = Message.media(response);
        reply(message);
    }

}
