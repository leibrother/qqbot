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
        TemplateService<?> service = context.getService(TemplateService.class);
        List<Plugin> plugins = pluginService.plugins();
        Map<String, String> states = new HashMap<>();
        plugins.forEach(plugin -> states.put(plugin.id(), "未启用"));
        pluginService.enabled().forEach(plugin -> states.put(plugin.id(), "已启用"));
        pluginService.disabled().forEach(plugin -> states.put(plugin.id(), "重启后禁用"));
        Map<String, Object> params = new HashMap<>();
        params.put("plugins", pluginService.plugins());
        params.put("states", states);
        Message markdown = service.markdown("templates/plugins.vm", params);
        context.reply(markdown);
    }

}
