package cc.rapidev.qqbot.boot.plugin.command;

import cc.rapidev.qqbot.api.model.Message;
import cc.rapidev.qqbot.boot.plugin.Plugin;
import cc.rapidev.qqbot.boot.plugin.PluginService;
import cc.rapidev.qqbot.extension.command.Command;
import cc.rapidev.qqbot.extension.command.CommandHandler;
import cc.rapidev.qqbot.extension.template.TemplateRenderer;
import cc.rapidev.qqbot.message.MessageContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
            List<Plugin> others = pluginService.enable(id);
            List<Plugin> enabled = pluginService.enabled();
            Map<String, Object> params = new HashMap<>();
            params.put("plugin", plugin);
            params.put("others", others);
            params.put("enabled", enabled);
            TemplateRenderer renderer = context.getService(TemplateRenderer.class);
            Message markdown = renderer.markdown("templates/plugins/enable_result.vm", params);
            context.reply(markdown);
        }
    }

}
