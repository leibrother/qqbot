package cc.rapidev.qqbot.adapter.webhook;

import cc.rapidev.qqbot.adapter.webhook.handler.WebhookHandler;

/**
 * @author leibrother
 */
public interface Webhook {

    static Webhook create() {
        return new VertxWebhook();
    }

    void open(int port);

    void close();

    void setHandler(int opcode, WebhookHandler handler);

}
