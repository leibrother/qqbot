package cc.rapidev.qqbot.adapter.webhook;

import cc.rapidev.qqbot.BotPayload;
import cc.rapidev.qqbot.adapter.webhook.handler.WebhookHandler;
import cc.rapidev.qqbot.common.utils.LogbackUtils;
import ch.qos.logback.classic.Level;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * @author leibrother
 */
public class VertxWebhook implements Webhook {

    private final Logger log = LoggerFactory.getLogger("[Bot Webhook]");

    private final Vertx vertx;
    private final HttpServer server;
    private final Map<Integer, WebhookHandler> handlers = new Hashtable<>();

    public VertxWebhook() {
        LogbackUtils.setLogLevel("io.vertx", Level.ERROR);
        LogbackUtils.setLogLevel("io.netty", Level.ERROR);
        this.vertx = Vertx.vertx();
        this.server = vertx.createHttpServer();
    }

    @Override
    public void open(int port) {
        Router router = Router.router(vertx);
        Route route = router.route("/webhook");
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
        server.requestHandler(router).listen(port);
    }

    private Map<String, Object> getHeaders(HttpServerRequest request) {
        Map<String, Object> headers = new HashMap<>();
        for (Map.Entry<String, String> header : request.headers()) {
            headers.put(header.getKey(), header.getValue());
        }
        return headers;
    }

    @Override
    public void close() {
        server.close();
    }

    @Override
    public void setHandler(int opcode, WebhookHandler handler) {
        this.handlers.put(opcode, handler);
    }

}
