package cc.rapidev.qqbot.extension.admin.command;

import cc.rapidev.qqbot.api.model.Message;
import cc.rapidev.qqbot.extension.admin.AdministratorService;
import cc.rapidev.qqbot.extension.command.Command;
import cc.rapidev.qqbot.extension.command.CommandHandler;
import cc.rapidev.qqbot.message.MessageContext;

/**
 * @author leibrother
 */
public class BecomeAdmin implements CommandHandler {

    private final AdministratorService service;

    public BecomeAdmin(AdministratorService service) {
        this.service = service;
    }

    @Override
    public void handle(MessageContext context, Command command) {
        String passwd = command.content();
        boolean result = service.become(context.author(), passwd);
        if (result) {
            context.reply(Message.text("您已成为管理员"));
        } else {
            context.reply(Message.text("认证失败"));
        }
    }

}
