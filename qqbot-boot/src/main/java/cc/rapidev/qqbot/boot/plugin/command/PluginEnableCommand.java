package cc.rapidev.qqbot.boot.plugin.command;

import cc.rapidev.qqbot.api.model.Message;
import cc.rapidev.qqbot.boot.plugin.Plugin;
import cc.rapidev.qqbot.boot.plugin.PluginService;
import cc.rapidev.qqbot.message.MessageContext;
import cc.rapidev.qqbot.message.matcher.MatcherMessageHandler;
import cc.rapidev.qqbot.message.matcher.MatcherService;

import java.util.List;
import java.util.Optional;

/**
 * @author leibrother
 */
public class PluginEnableCommand extends MatcherMessageHandler {

    public final PluginService pluginService;

    public PluginEnableCommand(PluginService pluginService) {
        this.pluginService = pluginService;
    }

    @Override
    public List<String> keywords() {
        return List.of("启用插件");
    }

    @Override
    public void process(MessageContext context, MatcherService matcher) {
        String name = matcher.current().trim();
        Optional<Plugin> optional = pluginService.get(name);
        if (optional.isEmpty()) {
            context.reply(Message.text("未找到插件: %s".formatted(name)));
        } else {
            pluginService.enable(name);
        }
    }

}
