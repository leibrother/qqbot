package cc.rapidev.qqbot.launcher.plugin.command;

import cc.rapidev.qqbot.api.model.Message;
import cc.rapidev.qqbot.api.model.MessageKeyboard;
import cc.rapidev.qqbot.api.model.MessageKeyboardButton;
import cc.rapidev.qqbot.extension.command.Command;
import cc.rapidev.qqbot.extension.command.CommandHandler;
import cc.rapidev.qqbot.launcher.plugin.Plugin;
import cc.rapidev.qqbot.launcher.plugin.PluginService;
import cc.rapidev.qqbot.launcher.plugin.view.PluginView;
import cc.rapidev.qqbot.message.MessageContext;

import java.util.Optional;

/**
 * @author leibrother
 */
public class PluginDetailHandler implements CommandHandler {

    private final PluginService pluginService;

    public PluginDetailHandler(PluginService pluginService) {
        this.pluginService = pluginService;
    }

    @Override
    public void handle(MessageContext context, Command command) {
        String id = command.content().trim();
        Optional<Plugin> optional = pluginService.get(id);
        if (optional.isEmpty()) {
            context.reply(Message.text("未找到插件: " + id));
            return;
        }
        Plugin plugin = optional.get();
        PluginView view = new PluginView(plugin);
        Message message = view.render();
        MessageKeyboard keyboard = new MessageKeyboard();
        if (pluginService.enabled().contains(plugin)) {
            keyboard.add(buildDisableButton(plugin));
        } else {
            keyboard.add(buildEnableButton(plugin));
        }
        message.keyboard(keyboard);
        context.reply(message);
    }

    private MessageKeyboardButton buildEnableButton(Plugin plugin) {
        return MessageKeyboardButton.command()
                .label("启用")
                .data("启用插件 %s".formatted(plugin.id()))
                .enter()
                .build();
    }

    private MessageKeyboardButton buildDisableButton(Plugin plugin) {
        return MessageKeyboardButton.command()
                .label("禁用")
                .data("禁用插件 %s".formatted(plugin.id()))
                .enter()
                .build();
    }

}
