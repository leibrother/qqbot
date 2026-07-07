package cc.rapidev.qqbot.server.adapter.webhook.handler;

import cc.rapidev.qqbot.Bot;
import cc.rapidev.qqbot.BotConfig;
import cc.rapidev.qqbot.BotPayload;
import cc.rapidev.qqbot.exception.BotException;
import cc.rapidev.qqbot.server.adapter.webhook.WebhookBotAdapter;

import java.util.Map;

/**
 * @author leibrother
 */
public abstract class WebhookHandler {

    private final WebhookBotAdapter botAdapter;

    public WebhookHandler(WebhookBotAdapter botAdapter) {
        this.botAdapter = botAdapter;
    }

    protected Bot getBot() {
        Bot bot = this.botAdapter.getBot();
        if (bot == null) {
            throw new BotException("the bot adapter is not bound to a bot");
        }
        return bot;
    }

    protected BotConfig getConfig() {
        Bot bot = getBot();
        return bot.config();
    }

    abstract public Object handle(Map<String, Object> headers, BotPayload payload);

}
