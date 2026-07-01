package cc.rapidev.qqbot.extension.settings.manager;

import cc.rapidev.qqbot.Bot;
import cc.rapidev.qqbot.extension.settings.SettingService;
import cc.rapidev.qqbot.message.MessageContext;

/**
 * @author leibrother
 */
public class ManagerService {

    private final ManagerSettingItem setting;

    public ManagerService(Bot bot) {
        this.setting = new ManagerSettingItem("manager", "管理员", "设置当前聊天的管理员");
        bot.use(SettingService.class).append(this.setting);
        bot.add(this);
    }

    public boolean isManager(MessageContext context) {
        return this.setting.isManager(context);
    }

}
