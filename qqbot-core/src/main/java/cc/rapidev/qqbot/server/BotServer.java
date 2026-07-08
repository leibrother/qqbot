package cc.rapidev.qqbot.server;

import cc.rapidev.qqbot.Bot;
import cc.rapidev.qqbot.common.interfaces.Disposable;
import cc.rapidev.qqbot.common.utils.LogbackUtils;
import ch.qos.logback.classic.Level;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.FaviconHandler;
import io.vertx.ext.web.handler.StaticHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.Optional;

/**
 * @author leibrother
 */
public class BotServer implements Disposable {

    private final Logger logger = LoggerFactory.getLogger(BotServer.class);
    private final URI accessibleUri;
    private final Vertx vertx;
    private final HttpServer httpServer;
    private final Router router;

    static {
        LogbackUtils.setLogLevel("io.vertx", Level.ERROR);
        LogbackUtils.setLogLevel("io.netty", Level.ERROR);
    }

    public BotServer(Bot bot) {
        this.accessibleUri = bot.config().getServerAccessibleUri();
        if (this.accessibleUri == null) {
            logger.warn("未配置可访问URI，部分服务无法使用！");
        }
        this.vertx = Vertx.vertx();
        this.httpServer = vertx.createHttpServer();
        this.router = Router.router(vertx);
        route("/favicon.ico").handler(FaviconHandler.create(vertx, "favicon.ico"));
        route("/resources/*").handler(StaticHandler.create("webroot"));
    }

    public Optional<URI> accessibleUri() {
        return Optional.ofNullable(this.accessibleUri);
    }

    public Route route(String path) {
        return this.router.route(path);
    }

    public Route route(String path, HttpMethod method) {
        return this.router.route(method, path);
    }

    public void run(int port) {
        this.httpServer.requestHandler(this.router).listen(port);
    }

    @Override
    public void destroy() {
        this.httpServer.close();
        this.vertx.close();
    }
}
