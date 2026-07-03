package cc.rapidev.qqbot.extension.settings;

import cc.rapidev.qqbot.Bot;
import cc.rapidev.qqbot.common.cache.CacheProxyFactory;
import cc.rapidev.qqbot.extension.Extension;
import cc.rapidev.qqbot.extension.command.CommandEntry;
import cc.rapidev.qqbot.extension.command.Keyword;
import cc.rapidev.qqbot.extension.settings.handler.SettingHandler;
import cc.rapidev.qqbot.extension.settings.manager.ManagerService;
import cc.rapidev.qqbot.extension.settings.persistence.CacheableSettingPersistenceService;
import cc.rapidev.qqbot.extension.settings.persistence.SettingPersistenceService;

/**
 * @author leibrother
 */
public class SettingsExtension implements Extension {

    @Override
    public void ready(Bot bot) {
        // 持久化服务
        SettingPersistenceService persistence = CacheProxyFactory.create(new CacheableSettingPersistenceService(bot));
        bot.add(persistence);
        // 设置服务
        SettingService service = new SettingService();
        bot.add(service);
        // 注册进入指令处理器
        SettingHandler handler = new SettingHandler(service);
        bot.dispatcher().register(handler);
        Keyword open = new Keyword("设置", "打开设置会话");
        bot.use(CommandEntry.class).add(open, handler);
        // 管理员服务
        new ManagerService(bot);
    }

    @Override
    public void destroy() {
    }

}
