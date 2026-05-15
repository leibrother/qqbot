package cc.rapidev.qqbot.boot.plugin.command;

import cc.rapidev.qqbot.api.model.Message;
import cc.rapidev.qqbot.boot.plugin.PluginService;
import cc.rapidev.qqbot.message.MessageContext;
import cc.rapidev.qqbot.message.matcher.MatcherMessageHandler;
import cc.rapidev.qqbot.message.matcher.MatcherService;
import cc.rapidev.qqbot.message.template.TemplateService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author leibrother
 */
public class PluginListCommand extends MatcherMessageHandler {

    private final PluginService pluginService;

    public PluginListCommand(PluginService pluginService) {
        this.pluginService = pluginService;
    }

    @Override
    public List<String> keywords() {
        return List.of("插件列表");
    }

    @Override
    public void process(MessageContext context, MatcherService matcher) {
        TemplateService<?> service = context.getService(TemplateService.class);
        Map<String, Object> params = new HashMap<>();
        params.put("plugins", pluginService.plugins());
        Message markdown = service.markdown("templates/plugins.vm", params);
        context.reply(markdown);
    }

}
