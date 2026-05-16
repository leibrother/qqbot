package cc.rapidev.qqbot;

import cc.rapidev.qqbot.adapter.BotAdapter;
import cc.rapidev.qqbot.api.BotApi;
import cc.rapidev.qqbot.api.model.Message;
import cc.rapidev.qqbot.api.model.MessageMedia;
import cc.rapidev.qqbot.api.model.User;
import cc.rapidev.qqbot.api.request.MessageRequest;
import cc.rapidev.qqbot.api.response.MessageMediaResponse;
import cc.rapidev.qqbot.api.response.MessageResponse;
import cc.rapidev.qqbot.common.Events;
import cc.rapidev.qqbot.common.Topic;
import cc.rapidev.qqbot.common.Version;
import cc.rapidev.qqbot.database.BotDatabase;
import cc.rapidev.qqbot.exception.BotException;
import cc.rapidev.qqbot.message.MessageDispatcher;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author leibrother
 */
public class Bot {

    public static final Version version = Version.parse("0.0.1");
    private final Logger log = LoggerFactory.getLogger("[Bot]");

    @Getter
    private final BotConfig config;
    private final BotAdapter adapter;
    @Getter
    private final MessageDispatcher dispatcher;
    @Getter
    private final BotApi api;
    @Getter
    private final BotDatabase database;
    @Getter
    private User info;

    public Bot() {
        this(BotConfig.create(), BotAdapter.create());
    }

    public Bot(BotConfig config) {
        this(config, BotAdapter.create());
    }

    public Bot(BotAdapter adapter) {
        this(BotConfig.create(), adapter);
    }

    public Bot(BotConfig config, BotAdapter adapter) {
        this.config = config;
        this.adapter = adapter;
        this.dispatcher = new MessageDispatcher(this);
        this.api = new BotApi(this);
        this.database = new BotDatabase(this);
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        log.info("Bot init...");
        this.adapter.bind(this);
        this.registerShutdownHook();
        User info = this.getApi().getAuthRequest().info();
        this.database.parameters().set("bot.name", info.getCleanUsername());
        this.info = info;
        log.info("Bot name is {}", info.getCleanUsername());
    }

    /**
     * 注册Shutdown钩子，当程序退出时关闭机器人
     */
    public void registerShutdownHook() {
        Thread shutdown = new Thread(this::stop);
        shutdown.setName("Shutdown");
        Runtime.getRuntime().addShutdownHook(shutdown);
    }

    /**
     * 机器人是否处于运行状态
     *
     * @return 机器人运行状态
     */
    public boolean isRunning() {
        return adapter.isRunning();
    }

    /**
     * 运行机器人
     */
    public void run() {
        this.run(true);
    }

    /**
     * 运行机器人，并设置是否保持活跃
     *
     * @param keepLive 是否保持活跃
     */
    public void run(boolean keepLive) {
        if (!adapter.isRunning()) {
            log.info("Bot startup...");
            adapter.run();
            consume(BotPayload.broadcast(Events.STARTED));
            if (keepLive) {
                keepLive();
            }
        }
    }

    /**
     * 停止机器人
     */
    public void stop() {
        if (adapter.isRunning()) {
            log.info("Bot stop running...");
            adapter.stop();
        }
    }

    /**
     * 保持活跃，避免主线程退出
     */
    @SuppressWarnings("BusyWait")
    private void keepLive() {
        Thread keepLiveThread = new Thread(() -> {
            while (isRunning()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        keepLiveThread.setName("Bot KeepLive");
        keepLiveThread.setDaemon(false);
        keepLiveThread.start();
        try {
            keepLiveThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 消费消息
     *
     * @param payload 消息内容
     */
    public void consume(BotPayload payload) {
        if (payload == null) {
            throw new IllegalArgumentException("payload is must not be null");
        }
        this.dispatcher.doDispatch(payload);
    }

    /**
     * 向指定主题发送消息
     *
     * @param topic   主题
     * @param message 消息内容
     * @return 响应结果
     */
    public MessageResponse sendMessage(Topic topic, Message message) {
        MessageRequest request = getApi().getMessageRequest();
        if (topic.isPrivate()) {
            return request.toUser(topic.getId(), message);
        } else if (topic.isGroupAt()) {
            return request.toGroup(topic.getId(), message);
        } else if (topic.isGuild() || topic.isGuildAt()) {
            return request.toChannel(topic.getId(), message);
        } else if (topic.isDirect()) {
            return request.toDirect(topic.getId(), message);
        } else {
            throw new BotException("current topic %s unable to send message".formatted(topic));
        }
    }

    /**
     * 向指定主题发送媒体消息
     *
     * @param topic 主题
     * @param media 媒体消息内容
     * @return 响应结果
     */
    public MessageMediaResponse sendMessage(Topic topic, MessageMedia media) {
        MessageRequest request = getApi().getMessageRequest();
        if (topic.isPrivate()) {
            return request.toUserMedia(topic.getId(), media);
        } else if (topic.isGroupAt()) {
            return request.toGroupMedia(topic.getId(), media);
        } else {
            throw new BotException("current topic %s unable to send media message".formatted(topic));
        }
    }

}
