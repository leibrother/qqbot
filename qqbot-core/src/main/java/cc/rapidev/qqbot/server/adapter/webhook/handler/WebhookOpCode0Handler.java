package cc.rapidev.qqbot.server.adapter.webhook.handler;

import cc.rapidev.qqbot.Bot;
import cc.rapidev.qqbot.BotPayload;
import cc.rapidev.qqbot.server.adapter.webhook.WebhookBotAdapter;

import java.util.Map;

/**
 * @author leibrother
 */
public class WebhookOpCode0Handler extends WebhookHandler {

    public WebhookOpCode0Handler(WebhookBotAdapter botAdapter) {
        super(botAdapter);
    }

    @Override
    public Object handle(Map<String, Object> headers, BotPayload payload) {
        Bot bot = getBot();
        bot.consume(payload);
        return BotPayload.webhookACK().json();
    }

}
