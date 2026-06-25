package cc.rapidev.qqbot.extension.settings;

import cc.rapidev.qqbot.Bot;
import cc.rapidev.qqbot.api.model.Message;
import cc.rapidev.qqbot.common.Context;
import cc.rapidev.qqbot.common.Topic;
import cc.rapidev.qqbot.extension.admin.AdministratorService;
import cc.rapidev.qqbot.extension.settings.component.SettingItem;
import cc.rapidev.qqbot.extension.settings.repository.SettingRepository;
import cc.rapidev.qqbot.extension.template.TemplateRenderer;
import cc.rapidev.qqbot.message.MessageContext;
import cc.rapidev.qqbot.message.model.Author;

/**
 * @author leibrother
 */
public class SettingSession {

    private final Bot bot;
    private final Topic topic;
    private final Author author;
    private Setting setting;

    public SettingSession(MessageContext context, Setting setting) {
        this.bot = context.bot();
        this.topic = context.topic();
        this.author = context.author();
        this.setting = setting;
    }

    /**
     * 渲染当前聚焦的设置项
     *
     * @return 消息体
     */
    public Message render() {
        SettingRepository repository = bot.use(SettingRepository.class);
        boolean admin = bot.use(AdministratorService.class).isAdmin(author);
        RenderContext context = new RenderContext(repository, topic, author, admin);
        Context ctx = Context.empty();
        ctx.set("setting", setting);
        ctx.set("content", setting.render(context));
        return bot.use(TemplateRenderer.class).render("/templates/settings/view.vm", ctx.map()).markdown();
    }

    /**
     * 回到当前聚焦的设置项的上一级并渲染
     *
     * @return 消息体
     */
    public Message back() {
        Setting parent = this.setting.parent();
        if (parent == null) {
            return Message.text("当前已是最上级");
        }
        this.setting = parent;
        return this.render();
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
            context.reply(this.render());
        } else if (setting instanceof SettingItem item) {
            SettingRepository repository = bot.use(SettingRepository.class);
            boolean result = item.set(context, repository, context.message());
            if (result) {
                context.reply(this.render());
            }
        }
    }

}
