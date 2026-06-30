package cc.rapidev.qqbot.server.adapter.websocket;

import cc.rapidev.qqbot.Bot;
import cc.rapidev.qqbot.server.adapter.BotAdapter;

/**
 * 机器人WebSocket适配器，使用WebSocket与机器人交互
 *
 * @author leibrother
 */
public class WebsocketBotAdapter implements BotAdapter {

    @Override
    public boolean isRunning() {
        return false;
    }

    @Override
    public void run(Bot bot) {

    }

    @Override
    public void destroy() {

    }

}
