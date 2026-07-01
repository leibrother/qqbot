package cc.rapidev.qqbot.extension.settings;

import cc.rapidev.qqbot.api.model.Message;
import cc.rapidev.qqbot.extension.settings.component.SettingItem;
import cc.rapidev.qqbot.extension.settings.repository.SettingRepository;
import cc.rapidev.qqbot.message.MessageContext;

/**
 * @author leibrother
 */
public class SettingSession {

    private Setting setting;

    public SettingSession(Setting setting) {
        this.setting = setting;
    }

    /**
     * 渲染当前聚焦的设置项
     *
     * @return 消息体
     */
    public Message render(MessageContext context) {
        SettingRepository repository = context.use(SettingRepository.class);
        SettingView view = new SettingView(setting, context);
        return view.render();
    }

    /**
     * 回到当前聚焦的设置项的上一级并渲染
     *
     * @return 消息体
     */
    public Message back(MessageContext context) {
        Setting parent = this.setting.parent();
        if (parent == null) {
            return Message.text("当前已是最上级");
        }
        this.setting = parent;
        return this.render(context);
    }

    /**
     * 处理用户消息
     * <p>如果当前聚焦的设置项是{@code SettingGroup}则查找下一级，否则执行赋值逻辑</p>
     *
     * @param context 消息上下文
     */
    public void proceed(MessageContext context) {
        if (setting instanceof SettingGroup group) {
            Setting next = group.findChild(context.message().content().trim());
            if (next == null) {
                context.reply(Message.text("未找到设置项：" + context.message().content()));
                return;
            }
            this.setting = next;
            context.reply(this.render(context));
        } else if (setting instanceof SettingItem item) {
            boolean result = item.set(context, context.message());
            if (result) {
                context.reply(this.render(context));
            }
        }
    }

}
