package cc.rapidev.qqbot.extension.settings;

import cc.rapidev.qqbot.Bot;
import cc.rapidev.qqbot.api.model.Message;
import cc.rapidev.qqbot.common.Context;
import cc.rapidev.qqbot.common.Topic;
import cc.rapidev.qqbot.extension.admin.AdministratorService;
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

    public Message render() {
        boolean admin = bot.use(AdministratorService.class).isAdmin(author);
        SettingRepository repository = bot.use(SettingRepository.class);
        Context ctx = Context.empty();
        ctx.set("setting", setting);
        ctx.set("content", setting.render(repository, topic, admin));
        return bot.use(TemplateRenderer.class).markdown("/templates/settings/view.vm", ctx.map());
    }

    public Message back() {
        Setting parent = this.setting.parent();
        if (parent == null) {
            return Message.text("当前已是最上级");
        }
        this.setting = parent;
        return this.render();
    }

    public void proceed(MessageContext context) {
        if (setting instanceof SettingGroup group) {
            Setting next = group.findNextItem(context.message().content().trim());
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
