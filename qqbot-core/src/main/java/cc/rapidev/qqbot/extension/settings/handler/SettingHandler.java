package cc.rapidev.qqbot.extension.settings.handler;

import cc.rapidev.qqbot.extension.command.Command;
import cc.rapidev.qqbot.extension.command.CommandHandler;
import cc.rapidev.qqbot.extension.settings.SettingService;
import cc.rapidev.qqbot.message.MessageContext;
import cc.rapidev.qqbot.message.MessageHandler;

/**
 * @author leibrother
 */
public class SettingHandler implements MessageHandler, CommandHandler {

    private final SettingService settingService;

    public SettingHandler(SettingService settingService) {
        this.settingService = settingService;
    }

    @Override
    public int order() {
        return 1000;
    }

    @Override
    public void handle(MessageContext context) {
        if (settingService.insession(context)) {
            settingService.following(context);
            context.complete();
        }
    }

    @Override
    public void handle(MessageContext context, Command command) {
        settingService.open(context);
    }

}
