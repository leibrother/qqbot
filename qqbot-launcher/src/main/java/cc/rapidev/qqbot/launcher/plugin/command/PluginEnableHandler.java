package cc.rapidev.qqbot.launcher.plugin.command;

import cc.rapidev.qqbot.api.model.Message;
import cc.rapidev.qqbot.extension.command.Command;
import cc.rapidev.qqbot.extension.command.admin.AdminCommandHandler;
import cc.rapidev.qqbot.launcher.plugin.Plugin;
import cc.rapidev.qqbot.launcher.plugin.PluginService;
import cc.rapidev.qqbot.launcher.plugin.view.PluginEnableResultView;
import cc.rapidev.qqbot.message.MessageContext;

import java.util.List;
import java.util.Optional;

/**
 * @author leibrother
 */
public class PluginEnableHandler implements AdminCommandHandler {

    public final PluginService pluginService;

    public PluginEnableHandler(PluginService pluginService) {
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
            List<Plugin> dependencies = pluginService.enable(id);
            List<Plugin> enabled = pluginService.enabled();
            PluginEnableResultView view = new PluginEnableResultView(plugin, dependencies, enabled);
            Message message = view.render();
            context.reply(message);
        }
    }

}
