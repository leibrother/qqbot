package cc.rapidev.qqbot.extension.settings;

import cc.rapidev.qqbot.Bot;
import cc.rapidev.qqbot.common.Events;
import cc.rapidev.qqbot.extension.Extension;
import cc.rapidev.qqbot.extension.command.CommandEntry;
import cc.rapidev.qqbot.extension.command.Keyword;
import cc.rapidev.qqbot.extension.settings.handler.SettingHandler;
import cc.rapidev.qqbot.extension.settings.repository.SettingRepository;

/**
 * @author leibrother
 */
public class SettingsExtension implements Extension {

    @Override
    public void ready(Bot bot) {
        SettingRepository repository = new SettingRepository(bot.database());
        bot.add(repository);
        SettingService service = new SettingService();
        bot.add(service);
        // 注册进入指令处理器
        SettingHandler handler = new SettingHandler(service);
        bot.dispatcher().register(Events.C2C_MESSAGE_CREATE, handler);
        Keyword open = new Keyword("设置", "打开设置会话");
        bot.use(CommandEntry.class).add(open, handler, Events.C2C_MESSAGE_CREATE);
    }

    @Override
    public void destroy() {
    }

}
