package cc.rapidev.qqbot.extension.settings.manager;

import cc.rapidev.qqbot.Bot;
import cc.rapidev.qqbot.extension.settings.SettingService;
import cc.rapidev.qqbot.message.MessageContext;
import cc.rapidev.qqbot.message.member.Member;

/**
 * @author leibrother
 */
public class ManagerService {

    private final ManagerSetting setting;

    public ManagerService(Bot bot) {
        this.setting = new ManagerSetting("manager", "管理员", "设置当前聊天的管理员");
        bot.use(SettingService.class).append(this.setting);
        bot.dispatcher().register(new ManagerHandler(this));
        bot.add(this);
    }

    public void add(MessageContext context, Member member, boolean supermanager) {
        this.setting.add(context, member, supermanager);
    }

    public void remove(MessageContext context, Member member) {
        this.setting.remove(context, member);
    }

    public void reset(MessageContext context) {
        this.setting.reset(context);
    }

    public boolean isManager(MessageContext context) {
        return this.setting.isManager(context);
    }

    public boolean isSupermanager(MessageContext context) {
        return this.setting.isSupermanager(context);
    }

}
