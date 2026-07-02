package cc.rapidev.qqbot.server;

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

/**
 * @author leibrother
 */
public class BotServer implements Disposable {

    private final Vertx vertx;
    private final HttpServer httpServer;
    private final Router router;

    static {
        LogbackUtils.setLogLevel("io.vertx", Level.ERROR);
        LogbackUtils.setLogLevel("io.netty", Level.ERROR);
    }

    public BotServer() {
        this.vertx = Vertx.vertx();
        this.httpServer = vertx.createHttpServer();
        this.router = Router.router(vertx);
        route("/favicon.ico").handler(FaviconHandler.create(vertx, "favicon.ico"));
        route("/resources/*").handler(StaticHandler.create("webroot"));
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
