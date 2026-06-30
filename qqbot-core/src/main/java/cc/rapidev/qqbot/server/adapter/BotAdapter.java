package cc.rapidev.qqbot.server.adapter;

import cc.rapidev.qqbot.Bot;
import cc.rapidev.qqbot.common.interfaces.Disposable;
import cc.rapidev.qqbot.server.adapter.webhook.WebhookBotAdapter;

/**
 * @author leibrother
 */
public interface BotAdapter extends Disposable {

    static BotAdapter create() {
        return new WebhookBotAdapter();
    }

    boolean isRunning();

    void run(Bot bot);

}
