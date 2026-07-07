package cc.rapidev.qqbot.extension.admin;

import cc.rapidev.qqbot.Bot;
import cc.rapidev.qqbot.extension.Extension;

/**
 * @author leibrother
 */
public class AdminExtension implements Extension {

    @Override
    public void ready(Bot bot) {
        AdminService service = new AdminService(bot);
        bot.add(service);
    }

    @Override
    public void destroy() throws Exception {

    }

}
