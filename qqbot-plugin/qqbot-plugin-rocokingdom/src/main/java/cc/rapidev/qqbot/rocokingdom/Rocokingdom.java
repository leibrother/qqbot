package cc.rapidev.qqbot.rocokingdom;

import cc.rapidev.qqbot.Bot;
import cc.rapidev.qqbot.extension.Extension;
import cc.rapidev.qqbot.extension.command.CommandEntry;
import cc.rapidev.qqbot.rocokingdom.command.CommandRegisterer;

/**
 * @author leibrother
 */
public class Rocokingdom implements Extension {

    @Override
    public void ready(Bot bot) {
        CommandRegisterer registerer = new CommandRegisterer(bot);
        bot.use(CommandEntry.class).register(registerer);
    }

    @Override
    public void destroy() {

    }

}
