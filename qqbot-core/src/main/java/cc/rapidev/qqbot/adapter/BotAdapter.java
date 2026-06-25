package cc.rapidev.qqbot.adapter;

import cc.rapidev.qqbot.Bot;
import cc.rapidev.qqbot.adapter.webhook.WebhookBotAdapter;
import cc.rapidev.qqbot.common.interfaces.Disposable;

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
