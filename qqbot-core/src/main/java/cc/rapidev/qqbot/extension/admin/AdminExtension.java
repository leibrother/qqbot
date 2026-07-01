package cc.rapidev.qqbot.extension.admin;

import cc.rapidev.qqbot.Bot;
import cc.rapidev.qqbot.extension.Extension;
import cc.rapidev.qqbot.extension.admin.command.CommandRegisterer;
import cc.rapidev.qqbot.extension.command.CommandEntry;

/**
 * @author leibrother
 */
public class AdminExtension implements Extension {

    @Override
    public void ready(Bot bot) {
        AdminService service = new AdminService(bot);
        bot.add(service);
        CommandRegisterer registerer = new CommandRegisterer(service);
        bot.use(CommandEntry.class).register(registerer);
    }

    @Override
    public void destroy() throws Exception {

    }

}
