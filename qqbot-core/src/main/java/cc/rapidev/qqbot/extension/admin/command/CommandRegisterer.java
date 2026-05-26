package cc.rapidev.qqbot.extension.admin.command;

import cc.rapidev.qqbot.extension.admin.AdministratorService;
import cc.rapidev.qqbot.extension.command.CommandHandlerSet;
import cc.rapidev.qqbot.extension.command.Keyword;
import cc.rapidev.qqbot.extension.command.KeywordRegisterer;

/**
 * @author leibrother
 */
public class CommandRegisterer implements KeywordRegisterer {

    private final AdministratorService service;

    public CommandRegisterer(AdministratorService service) {
        this.service = service;
    }

    @Override
    public void register(CommandHandlerSet entry) {
        entry.add(new Keyword("管理员认证"), new BecomeAdmin(service));
    }

}
