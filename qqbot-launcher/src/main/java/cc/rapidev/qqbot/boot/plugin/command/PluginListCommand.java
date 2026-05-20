package cc.rapidev.qqbot.boot.plugin.command;

import cc.rapidev.qqbot.api.model.Message;
import cc.rapidev.qqbot.boot.plugin.Plugin;
import cc.rapidev.qqbot.boot.plugin.PluginService;
import cc.rapidev.qqbot.message.MessageContext;
import cc.rapidev.qqbot.message.command.Command;
import cc.rapidev.qqbot.message.command.CommandHandler;
import cc.rapidev.qqbot.message.template.TemplateService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author leibrother
 */
public class PluginListCommand implements CommandHandler {

    private final PluginService pluginService;

    public PluginListCommand(PluginService pluginService) {
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
        Map<String, Object> params = new HashMap<>();
        params.put("plugins", plugins);
        params.put("statuses", statuses);

        TemplateService<?> service = context.getService(TemplateService.class);
        Message markdown = service.markdown("templates/plugins/list.vm", params);
        context.reply(markdown);
    }

}
