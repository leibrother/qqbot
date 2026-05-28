package cc.rapidev.qqbot.extension.settings;

import cc.rapidev.qqbot.Bot;
import cc.rapidev.qqbot.api.model.Message;
import cc.rapidev.qqbot.extension.command.Command;
import cc.rapidev.qqbot.extension.command.CommandHandlerSet;
import cc.rapidev.qqbot.extension.settings.item.Input;
import cc.rapidev.qqbot.extension.settings.item.Selection;
import cc.rapidev.qqbot.extension.settings.repository.SettingRepository;
import cc.rapidev.qqbot.message.MessageContext;
import cc.rapidev.qqbot.message.model.MessageGeneric;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author leibrother
 */
public class SettingService {

    private final Bot bot;
    private final SettingGroup root;
    private final SettingRepository repository;
    private final CommandHandlerSet handler;
    private final Map<String, SettingSession> sessions = Maps.newConcurrentMap();

    public SettingService(Bot bot) {
        this.bot = bot;
        this.root = new SettingGroup("root", "设置", "选择设置项");
        this.repository = new SettingRepository(bot.database());
        this.handler = new CommandHandlerSet((context, _) -> this.proceed(context));
        this.handler.add("返回", (context, _) -> this.back(context));
        this.handler.add("退出设置", (context, _) -> this.exit(context));
        loadTestData();
    }

    private void loadTestData() {
        SettingGroup groupA = new SettingGroup("a", "模块A", "这是设置项描述");
        groupA.append(new Input("a", "姓名", "设置您的姓名", SettingScope.TOPIC));
        groupA.append(new Input("b", "性别", "设置您的性别", SettingScope.TOPIC));
        groupA.append(new Input("c", "年龄", "设置您的年龄", SettingScope.TOPIC));
        this.root.append(groupA);
        SettingGroup groupB = new SettingGroup("b", "模块B", "这是设置项描述");
        groupB.append(new Selection("a", "提供商", "选择模型提供商", List.of("deepseek", "google gemini"), SettingScope.TOPIC));
        this.root.append(groupB);
        this.root.append(new SettingGroup("c", "模块C", "这是设置项描述"));
    }

    public boolean insession(MessageContext context) {
        Optional<String> optional = context.usid();
        return optional.filter(sessions::containsKey).isPresent();
    }

    public void open(MessageContext context) {
        Optional<String> optional = context.usid();
        if (optional.isEmpty()) {
            return;
        }
        SettingSession session = sessions.computeIfAbsent(optional.get(), (_) -> new SettingSession(context, root));
        Message render = session.render();
        context.reply(render);
    }

    public void exit(MessageContext context) {
        context.usid().ifPresent(usid -> {
            if (sessions.containsKey(usid)) {
                sessions.remove(usid);
                context.reply(Message.text("已退出设置"));
            }
        });
    }

    public void back(MessageContext context) {
        context.usid().ifPresent(usid -> {
            SettingSession session = sessions.get(usid);
            if (session == null) {
                return;
            }
            Message message = session.back();
            if (message != null) {
                context.reply(message);
            }
        });
    }

    public void proceed(MessageContext context) {
        context.usid().ifPresent(usid -> {
            SettingSession session = sessions.get(usid);
            if (session == null) {
                return;
            }
            session.proceed(context);
        });
    }

    public void following(MessageContext context) {
        MessageGeneric message = context.message();
        Command command = new Command(message.content());
        this.handler.handle(context, command);
    }

}
