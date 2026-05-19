package cc.rapidev.qqbot.boot.plugin.command;

import cc.rapidev.qqbot.api.model.Message;
import cc.rapidev.qqbot.boot.plugin.Plugin;
import cc.rapidev.qqbot.boot.plugin.PluginService;
import cc.rapidev.qqbot.message.MessageContext;
import cc.rapidev.qqbot.message.command.Command;
import cc.rapidev.qqbot.message.command.CommandHandler;

import java.util.List;
import java.util.Optional;

/**
 * @author leibrother
 */
public class PluginEnableCommand implements CommandHandler {

    public final PluginService pluginService;

    public PluginEnableCommand(PluginService pluginService) {
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
            List<Plugin> enables = pluginService.enable(id);

        }
    }

}
