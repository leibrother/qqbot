package cc.rapidev.qqbot.extension.command;

import cc.rapidev.qqbot.Bot;
import cc.rapidev.qqbot.common.Events;
import cc.rapidev.qqbot.extension.Extension;
import cc.rapidev.qqbot.message.MessageDispatcher;

/**
 * @author leibrother
 */
public class CommandExtension implements Extension {

    private CommandEntry entry;

    @Override
    public void ready(Bot bot) {
        this.entry = new CommandEntry();
        bot.add(entry);
        MessageDispatcher dispatcher = bot.dispatcher();
        Events.messageCreateEvents.forEach(e -> dispatcher.register(e, entry));
    }

    @Override
    public void destroy() {
    }

}
