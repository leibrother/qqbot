package cc.rapidev.qqbot.extension.settings;

import cc.rapidev.qqbot.api.model.Message;
import cc.rapidev.qqbot.extension.command.Command;
import cc.rapidev.qqbot.extension.command.CommandHandlerSet;
import cc.rapidev.qqbot.extension.settings.component.Button;
import cc.rapidev.qqbot.extension.settings.component.Input;
import cc.rapidev.qqbot.extension.settings.component.Selection;
import cc.rapidev.qqbot.message.MessageContext;
import cc.rapidev.qqbot.message.model.MessageGeneric;
import com.google.common.collect.Maps;

import java.util.Map;
import java.util.Optional;

/**
 * @author leibrother
 */
public class SettingService {

    private final SettingGroup root;
    private final CommandHandlerSet handler;
    private final Map<String, SettingSession> sessions = Maps.newConcurrentMap();

    public SettingService() {
        this.root = new SettingGroup("root", "设置", "选择设置项");
        this.handler = new CommandHandlerSet((context, _) -> this.proceed(context));
        this.handler.add("返回", (context, _) -> this.back(context));
        this.handler.add("退出设置", (context, _) -> this.exit(context));
        loadTestData();
    }

    private void loadTestData() {
        SettingGroup components = new SettingGroup("components", "组件测试", "测试内置组件");
        components.append(
                Input.builder()
                        .key("input")
                        .name("输入")
                        .description("这是一个普通输入组件")
                        .build()
        );
        components.append(
                Input.builder()
                        .key("input-password")
                        .name("密码输入")
                        .description("这是一个会将内容隐藏的输入组件")
                        .password()
                        .build()
        );
        components.append(
                Selection.builder()
                        .key("selection")
                        .name("选择")
                        .description("这是一个单选组件")
                        .addOption("Option A")
                        .addOption("Option B")
                        .build()
        );
        components.append(
                Button.builder()
                        .key("button")
                        .name("按钮")
                        .description("这是一个普通的按钮")
                        .onclick(_ -> "你点击了按钮")
                        .build()
        );
        this.root.append(components);
    }

    /**
     * 当前消息上下文是否在会话中（通过USID判断）
     *
     * @param context 消息上下文
     * @return true/false
     */
    public boolean insession(MessageContext context) {
        Optional<String> optional = context.usid();
        return optional.filter(sessions::containsKey).isPresent();
    }

    /**
     * 开启设置会话
     *
     * @param context 消息上下文
     */
    public void open(MessageContext context) {
        Optional<String> optional = context.usid();
        if (optional.isEmpty()) {
            return;
        }
        SettingSession session = sessions.computeIfAbsent(optional.get(), (_) -> new SettingSession(context, root));
        Message render = session.render();
        context.reply(render);
    }

    /**
     * 退出设置会话
     *
     * @param context 消息上下文
     */
    public void exit(MessageContext context) {
        context.usid().ifPresent(usid -> {
            if (sessions.containsKey(usid)) {
                sessions.remove(usid);
                context.reply(Message.text("已退出设置"));
            }
        });
    }

    /**
     * 返回上一级
     *
     * @param context 消息上下文
     */
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

    /**
     * 继续会话（返回、退出会话或者交给设置会话实例处理）
     *
     * @param context 消息上下文
     */
    public void following(MessageContext context) {
        MessageGeneric message = context.message();
        Command command = new Command(message.content());
        this.handler.handle(context, command);
    }

    /**
     * 交给设置会话实例进行处理（设置值、进入下一级、等等）
     *
     * @param context 消息上下文
     */
    public void proceed(MessageContext context) {
        context.usid().ifPresent(usid -> {
            SettingSession session = sessions.get(usid);
            if (session == null) {
                return;
            }
            session.proceed(context);
        });
    }

}
