package cc.rapidev.qqbot.adapter.webhook;

import cc.rapidev.qqbot.BotPayload;
import cc.rapidev.qqbot.adapter.webhook.handler.WebhookHandler;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.handler.BodyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * @author leibrother
 */
public class Webhook {

    private final Logger log = LoggerFactory.getLogger(Webhook.class);
    private final Map<Integer, WebhookHandler> handlers = new Hashtable<>();

    public Webhook() {
    }

    public void open(Route route) {
        route.handler(BodyHandler.create());
        route.handler(ctx -> {
            String body = ctx.body().asString();
            log.debug("received: {}", body);
            BotPayload payload = new BotPayload(body);
            WebhookHandler handler = this.handlers.get(payload.opcode());
            if (handler != null) {
                Map<String, Object> headers = getHeaders(ctx.request());
                Object result = handler.handle(headers, payload);
                if (result != null) {
                    ctx.json(result);
                }
            }
        });
    }

    private Map<String, Object> getHeaders(HttpServerRequest request) {
        Map<String, Object> headers = new HashMap<>();
        for (Map.Entry<String, String> header : request.headers()) {
            headers.put(header.getKey(), header.getValue());
        }
        return headers;
    }

    public void setHandler(int opcode, WebhookHandler handler) {
        this.handlers.put(opcode, handler);
    }

}
