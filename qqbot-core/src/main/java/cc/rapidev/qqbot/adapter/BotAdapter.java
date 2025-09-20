package cc.rapidev.qqbot.adapter;

import cc.rapidev.qqbot.Bot;
import cc.rapidev.qqbot.adapter.webhook.WebhookBotAdapter;

/**
 * @author leibrother
 */
public interface BotAdapter {

    static BotAdapter create() {
        return new WebhookBotAdapter();
    }

    void bind(Bot bot);

    boolean isRunning();

    void run();

    void stop();

}
