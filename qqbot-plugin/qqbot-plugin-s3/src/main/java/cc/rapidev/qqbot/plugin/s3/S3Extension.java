package cc.rapidev.qqbot.plugin.s3;

import cc.rapidev.qqbot.Bot;
import cc.rapidev.qqbot.extension.Extension;
import cc.rapidev.qqbot.extension.settings.SettingGroup;
import cc.rapidev.qqbot.extension.settings.SettingService;
import cc.rapidev.qqbot.plugin.s3.service.S3SettingService;

/**
 * @author leibrother
 */
public class S3Extension implements Extension {

    S3SettingService s3SettingService;

    @Override
    public void ready(Bot bot) {
        SettingGroup group = new SettingGroup("oss", "对象存储", "配置兼容AWS S3的对象存储服务");
        bot.use(SettingService.class).append(group);
        this.s3SettingService = new S3SettingService(bot, group);
        try {
            this.s3SettingService.apply();
        } catch (Exception ignore) {
        }
    }

    @Override
    public void destroy() throws Exception {
    }

}
