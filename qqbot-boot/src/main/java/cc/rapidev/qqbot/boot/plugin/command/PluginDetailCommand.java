package cc.rapidev.qqbot.boot.plugin.command;

import cc.rapidev.qqbot.api.model.Message;
import cc.rapidev.qqbot.api.model.MessageKeyboard;
import cc.rapidev.qqbot.api.model.MessageKeyboardButton;
import cc.rapidev.qqbot.boot.plugin.Plugin;
import cc.rapidev.qqbot.boot.plugin.PluginService;
import cc.rapidev.qqbot.message.MessageContext;
import cc.rapidev.qqbot.message.matcher.MatcherMessageHandler;
import cc.rapidev.qqbot.message.matcher.MatcherService;
import cc.rapidev.qqbot.message.template.TemplateService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author leibrother
 */
public class PluginDetailCommand extends MatcherMessageHandler {

    private final PluginService pluginService;

    public PluginDetailCommand(PluginService pluginService) {
        this.pluginService = pluginService;
    }

    @Override
    public List<String> keywords() {
        return List.of("插件详情");
    }

    @Override
    public void process(MessageContext context, MatcherService matcher) {
        String name = matcher.current().trim();
        Optional<Plugin> optional = pluginService.get(name);
        if (optional.isEmpty()) {
            context.reply(Message.text("未找到插件: " + name));
            return;
        }

        Map<String, Object> params = new HashMap<>();
        params.put("plugin", optional.get());
        TemplateService<?> service = context.getService(TemplateService.class);
        Message markdown = service.markdown("templates/plugin.vm", params);
        MessageKeyboard keyboard = new MessageKeyboard();
        keyboard.add(buildEnableButton(name));
        markdown.keyboard(keyboard);
        context.reply(markdown);
    }

    private MessageKeyboardButton buildEnableButton(String name) {
        return MessageKeyboardButton.command()
                .label("启用")
                .data("启用插件 %s".formatted(name))
                .enter(true)
                .build();
    }

    private MessageKeyboardButton buildDisableButton(String name) {
        return MessageKeyboardButton.command()
                .label("禁用")
                .data("禁用插件 %s".formatted(name))
                .enter(true)
                .build();
    }

}
