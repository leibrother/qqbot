package cc.rapidev.qqbot.extension.command.admin;

import cc.rapidev.qqbot.api.model.Message;
import cc.rapidev.qqbot.extension.admin.AdminService;
import cc.rapidev.qqbot.extension.command.*;
import cc.rapidev.qqbot.message.MessageContext;

/**
 * @author leibrother
 */
public class AuthHandler implements CommandHandler, KeywordRegisterer {

    @Override
    public void handle(MessageContext context, Command command) {
        String passwd = command.content().trim();
        AdminService service = context.use(AdminService.class);
        if (service.isAdmin(context.author())) {
            context.reply(Message.text("您已是管理员，请勿重复操作！"));
        } else if (passwd.isEmpty()) {
            context.reply(Message.text("请输入认证码"));
        } else if (service.become(context.author(), passwd)) {
            context.reply(Message.text("您已成为管理员"));
        } else {
            context.reply(Message.text("认证失败"));
        }
    }

    @Override
    public void register(CommandHandlerSet entry) {
        entry.add(new Keyword("认证", "认证成为机器人管理员"), this);
    }

}
