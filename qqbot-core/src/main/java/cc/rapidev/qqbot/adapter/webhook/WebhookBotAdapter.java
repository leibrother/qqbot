package cc.rapidev.qqbot.adapter.webhook;

import cc.rapidev.qqbot.Bot;
import cc.rapidev.qqbot.adapter.BotAdapter;
import cc.rapidev.qqbot.adapter.webhook.handler.WebhookOpCode0Handler;
import cc.rapidev.qqbot.adapter.webhook.handler.WebhookOpCode13Handler;
import cc.rapidev.qqbot.common.Constant;
import cc.rapidev.qqbot.exception.BotException;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 机器人Webhook适配器，用于处理Webhook回调
 *
 * @author leibrother
 */
public class WebhookBotAdapter implements BotAdapter {

    private final Logger log = LoggerFactory.getLogger("[Bot Webhook Adapter]");

    private volatile boolean running = false;
    private final Webhook webhook;
    @Getter
    private Bot bot;

    public WebhookBotAdapter() {
        this(Webhook.create());
    }

    public WebhookBotAdapter(Webhook webhook) {
        this.webhook = webhook;
        this.webhook.setHandler(0, new WebhookOpCode0Handler(this));
        this.webhook.setHandler(13, new WebhookOpCode13Handler(this));
    }

    @Override
    public void bind(Bot bot) {
        this.bot = bot;
    }

    @Override
    public boolean isRunning() {
        return this.running;
    }

    @Override
    public void run() {
        if (!isRunning()) {
            synchronized (this) {
                if (!isRunning()) {
                    log.info("Webhook startup...");
                    int port = getPort();
                    this.webhook.open(port);
                    this.running = true;
                    log.info("Webhook started. port: {}", port);
                }
            }
        }
    }

    @Override
    public void stop() {
        if (isRunning()) {
            synchronized (this) {
                if (isRunning()) {
                    log.info("Bot webhook stoping...");
                    this.webhook.close();
                    this.running = false;
                }
            }
        }
    }

    private int getPort() {
        String property = System.getProperty(Constant.PROPERTY_WEBHOOK_PORT, "8080");
        int port = Integer.parseInt(property);
        if (port < 1 || port > 65535) {
            throw new BotException("webhook port must be between 1 and 65535");
        }
        return port;
    }

}
