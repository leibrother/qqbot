package cc.rapidev.qqbot.extension.push;

import cc.rapidev.qqbot.Bot;
import cc.rapidev.qqbot.common.cache.CacheProxyFactory;
import cc.rapidev.qqbot.extension.Extension;
import cc.rapidev.qqbot.extension.push.handler.AutoCloseHandler;
import cc.rapidev.qqbot.extension.settings.SettingService;

/**
 * @author leibrother
 */
public class PushExtension implements Extension {

    @Override
    public void ready(Bot bot) {
        PushService service = CacheProxyFactory.create(new PushServiceCacheable(bot));
        bot.add(service);
        PushSetting setting = new PushSetting(service);
        bot.use(SettingService.class).append(setting);
        bot.dispatcher().register(new AutoCloseHandler(service));
    }

    @Override
    public void destroy() throws Exception {
    }

}
