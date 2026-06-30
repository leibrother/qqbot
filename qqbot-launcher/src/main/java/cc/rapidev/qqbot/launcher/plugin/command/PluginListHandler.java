package cc.rapidev.qqbot.launcher.plugin.command;

import cc.rapidev.qqbot.extension.command.Command;
import cc.rapidev.qqbot.extension.command.CommandHandler;
import cc.rapidev.qqbot.launcher.plugin.Plugin;
import cc.rapidev.qqbot.launcher.plugin.PluginService;
import cc.rapidev.qqbot.launcher.plugin.view.PluginListView;
import cc.rapidev.qqbot.message.MessageContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author leibrother
 */
public class PluginListHandler implements CommandHandler {

    private final PluginService pluginService;

    public PluginListHandler(PluginService pluginService) {
        this.pluginService = pluginService;
    }

    @Override
    public void handle(MessageContext context, Command command) {
        List<Plugin> plugins = pluginService.plugins();
        List<Plugin> enabled = pluginService.enabled();
        List<Plugin> disabled = pluginService.disabled();
        if (command.match("已启用").isPresent()) {
            plugins = plugins.stream().filter(enabled::contains).toList();
        } else if (command.match("未启用").isPresent()) {
            plugins = plugins.stream().filter(plugin -> !enabled.contains(plugin)).toList();
        }
        Map<String, String> statuses = new HashMap<>();
        plugins.forEach(plugin -> statuses.put(plugin.id(), "未启用"));
        enabled.forEach(plugin -> statuses.put(plugin.id(), "已启用"));
        disabled.forEach(plugin -> statuses.put(plugin.id(), "重启后禁用"));
        PluginListView view = new PluginListView(plugins, statuses);
        context.reply(view.render());
    }

}
