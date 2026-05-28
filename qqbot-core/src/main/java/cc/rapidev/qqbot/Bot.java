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
import cc.rapidev.qqbot.common.service.ServiceRegistrationCenter;
import cc.rapidev.qqbot.common.utils.version.Version;
import cc.rapidev.qqbot.database.BotDatabase;
import cc.rapidev.qqbot.database.repository.parameter.ParameterRepository;
import cc.rapidev.qqbot.exception.BotException;
import cc.rapidev.qqbot.extension.Extension;
import cc.rapidev.qqbot.extension.ExtensionManager;
import cc.rapidev.qqbot.message.MessageDispatcher;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author leibrother
 */
public class Bot extends ServiceRegistrationCenter {

    public static final Version version = Version.parse("0.0.1");
    private final Logger log = LoggerFactory.getLogger("[Bot]");

    @Getter
    private final BotConfig config;
    private final BotAdapter adapter;
    private final MessageDispatcher dispatcher;
    private final BotApi api;
    private final BotDatabase database;
    private final ExtensionManager extensionManager;
    @Getter
    private User info;
    private volatile boolean shutdown = false;

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
        this.extensionManager = new ExtensionManager(this);
        init();
    }

    public BotApi api() {
        return this.api;
    }

    public BotDatabase database() {
        return this.database;
    }

    public ParameterRepository parameters() {
        return this.database.parameters();
    }

    public MessageDispatcher dispatcher() {
        return this.dispatcher;
    }

    /**
     * 初始化
     */
    private void init() {
        log.info("Bot initializing...");
        this.adapter.bind(this);
        this.registerShutdownHook();
        User info = this.api().getAuthRequest().info();
        this.parameters().set("bot.name", info.getCleanUsername());
        this.info = info;
        log.info("Bot name is {}", info.getCleanUsername());
    }

    /**
     * 注册Shutdown钩子，当程序退出时关闭机器人
     */
    private void registerShutdownHook() {
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
     * 机器人是否已停机
     *
     * @return 机器人停机状态
     */
    public boolean isShutdown() {
        return this.shutdown;
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
        if (isShutdown()) {
            throw new BotException("bot is shutdown");
        } else if (!adapter.isRunning()) {
            consume(BotPayload.broadcast(Events.START));
            this.extensionManager.init();
            this.adapter.run();
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
            log.info("Bot stopping...");
            adapter.stop();
            this.extensionManager.destroy();
            this.dispatcher.destroy();
            this.shutdown = true;
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
     * 安装扩展
     *
     * @param extension 扩展
     */
    public void install(Class<? extends Extension> extension) {
        this.extensionManager.declare(extension);
    }

    /**
     * 消费消息
     *
     * @param payload 消息内容
     */
    public void consume(BotPayload payload) {
        if (payload == null) {
            throw new IllegalArgumentException("payload must not be null");
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
        MessageRequest request = api().getMessageRequest();
        if (topic.isPrivate()) {
            return request.toUser(topic.id(), message);
        } else if (topic.isGroupAt()) {
            return request.toGroup(topic.id(), message);
        } else if (topic.isGuild() || topic.isGuildAt()) {
            return request.toChannel(topic.id(), message);
        } else if (topic.isDirect()) {
            return request.toDirect(topic.id(), message);
        } else {
            throw new BotException("unable to send message to topic %s".formatted(topic));
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
        MessageRequest request = api().getMessageRequest();
        if (topic.isPrivate()) {
            return request.toUserMedia(topic.id(), media);
        } else if (topic.isGroupAt()) {
            return request.toGroupMedia(topic.id(), media);
        } else {
            throw new BotException("unable to send media message to topic %s".formatted(topic));
        }
    }

}
