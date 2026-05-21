package cc.rapidev.qqbot.extension.listener;

import cc.rapidev.qqbot.Bot;
import cc.rapidev.qqbot.common.Events;
import cc.rapidev.qqbot.extension.Extension;
import cc.rapidev.qqbot.message.MessageDispatcher;

/**
 * @author leibrother
 */
public class ListenerExtension implements Extension {

    public ListenerExtension(Bot bot) {
        MessageDispatcher dispatcher = bot.dispatcher();
        StartListener start = new StartListener();
        StartedListener started = new StartedListener(start);
        dispatcher.register(Events.START, start);
        dispatcher.register(Events.STARTED, started);
    }

    @Override
    public void destroy() {
    }

}
