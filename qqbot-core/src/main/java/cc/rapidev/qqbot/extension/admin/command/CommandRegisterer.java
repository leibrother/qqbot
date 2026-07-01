package cc.rapidev.qqbot.extension.admin.command;

import cc.rapidev.qqbot.extension.admin.AdminService;
import cc.rapidev.qqbot.extension.command.CommandHandlerSet;
import cc.rapidev.qqbot.extension.command.Keyword;
import cc.rapidev.qqbot.extension.command.KeywordRegisterer;

/**
 * @author leibrother
 */
public class CommandRegisterer implements KeywordRegisterer {

    private final AdminService service;

    public CommandRegisterer(AdminService service) {
        this.service = service;
    }

    @Override
    public void register(CommandHandlerSet entry) {
        entry.add(new Keyword("认证", "认证成为机器人管理员"), new AuthenticationHandler(service));
    }

}
