package cc.rapidev.qqbot.server.adapter.webhook;

import cc.rapidev.qqbot.Bot;
import cc.rapidev.qqbot.server.adapter.BotAdapter;
import cc.rapidev.qqbot.server.adapter.webhook.handler.WebhookOpCode0Handler;
import cc.rapidev.qqbot.server.adapter.webhook.handler.WebhookOpCode13Handler;
import io.vertx.ext.web.Route;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 机器人Webhook适配器，用于处理Webhook回调
 *
 * @author leibrother
 */
public class WebhookBotAdapter implements BotAdapter {

    private final Logger log = LoggerFactory.getLogger(WebhookBotAdapter.class);

    private volatile boolean running = false;
    private final Webhook webhook;
    @Getter
    private Bot bot;
    private Route route;

    public WebhookBotAdapter() {
        this.webhook = new Webhook();
        this.webhook.setHandler(0, new WebhookOpCode0Handler(this));
        this.webhook.setHandler(13, new WebhookOpCode13Handler(this));
    }

    @Override
    public boolean isRunning() {
        return this.running;
    }

    @Override
    public synchronized void run(Bot bot) {
        if (this.isRunning()) {
            throw new IllegalStateException("webhook adapter is already running");
        }
        log.info("Webhook starting...");
        this.bot = bot;
        this.route = bot.server().route("/webhook");
        this.webhook.open(this.route);
        this.running = true;
    }

    @Override
    public void destroy() {
        if (isRunning()) {
            synchronized (this) {
                if (isRunning()) {
                    log.info("Bot webhook stopping...");
                    this.running = false;
                    if (this.route != null) {
                        this.route.remove();
                        this.route = null;
                    }
                }
            }
        }
    }

}
