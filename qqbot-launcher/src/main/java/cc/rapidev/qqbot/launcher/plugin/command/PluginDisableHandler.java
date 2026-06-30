package cc.rapidev.qqbot.launcher.plugin.command;

import cc.rapidev.qqbot.api.model.Message;
import cc.rapidev.qqbot.extension.command.Command;
import cc.rapidev.qqbot.extension.command.CommandHandler;
import cc.rapidev.qqbot.launcher.plugin.Plugin;
import cc.rapidev.qqbot.launcher.plugin.PluginService;
import cc.rapidev.qqbot.message.MessageContext;

import java.util.Optional;

/**
 * @author leibrother
 */
public class PluginDisableHandler implements CommandHandler {

    private final PluginService pluginService;

    public PluginDisableHandler(PluginService pluginService) {
        this.pluginService = pluginService;
    }

    @Override
    public void handle(MessageContext context, Command command) {
        String id = command.content().trim();
        Optional<Plugin> optional = pluginService.get(id);
        if (optional.isEmpty()) {
            context.reply(Message.text("未找到插件: %s".formatted(id)));
        } else {
            Plugin plugin = optional.get();
            pluginService.disable(plugin.id());
            context.reply(Message.text("插件%s已标记，重启后将禁用此插件".formatted(plugin.name())));
        }
    }

}
