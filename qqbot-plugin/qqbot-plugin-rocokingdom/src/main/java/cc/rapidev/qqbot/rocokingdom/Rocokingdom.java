package cc.rapidev.qqbot.rocokingdom;

import cc.rapidev.qqbot.Bot;
import cc.rapidev.qqbot.extension.Extension;
import cc.rapidev.qqbot.extension.command.CommandEntry;
import cc.rapidev.qqbot.extension.command.CommandHandlerSet;
import cc.rapidev.qqbot.extension.command.Keyword;
import cc.rapidev.qqbot.extension.settings.SettingGroup;
import cc.rapidev.qqbot.extension.settings.SettingService;
import cc.rapidev.qqbot.rocokingdom.service.MerchantService;

/**
 * @author leibrother
 */
public class Rocokingdom implements Extension {

    @Override
    public void ready(Bot bot) {
        // CommandRegisterer registerer = new CommandRegisterer(bot);
        // bot.use(CommandEntry.class).register(registerer);

        // 命令组
        CommandHandlerSet mainCommand = new CommandHandlerSet();
        bot.use(CommandEntry.class).add(new Keyword("洛克", "洛克王国世界"), mainCommand);
        // 设置组
        SettingGroup mainSetting = new SettingGroup("rocokingdom", "洛克王国世界", "洛克王国世界");
        bot.use(SettingService.class).append(mainSetting);

        new MerchantService(bot, mainCommand, mainSetting);
    }

    @Override
    public void destroy() {

    }

}
