package cc.rapidev.qqbot.boot.plugin.command;

import cc.rapidev.qqbot.api.model.Message;
import cc.rapidev.qqbot.api.model.MessageKeyboard;
import cc.rapidev.qqbot.api.model.MessageKeyboardButton;
import cc.rapidev.qqbot.boot.plugin.Plugin;
import cc.rapidev.qqbot.boot.plugin.PluginService;
import cc.rapidev.qqbot.message.MessageContext;
import cc.rapidev.qqbot.message.command.Command;
import cc.rapidev.qqbot.message.command.CommandHandler;
import cc.rapidev.qqbot.message.template.TemplateService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author leibrother
 */
public class PluginDetailCommand implements CommandHandler {

    private final PluginService pluginService;

    public PluginDetailCommand(PluginService pluginService) {
        this.pluginService = pluginService;
    }

    @Override
    public void handle(MessageContext context, Command command) {
        String id = command.content().trim();
        Optional<Plugin> optional = pluginService.get(id);
        if (optional.isEmpty()) {
            context.reply(Message.text("未找到插件: " + id));
            return;
        }
        Plugin plugin = optional.get();
        Map<String, Object> params = new HashMap<>();
        params.put("plugin", optional.get());
        TemplateService<?> service = context.getService(TemplateService.class);
        Message markdown = service.markdown("templates/plugins/detail.vm", params);
        MessageKeyboard keyboard = new MessageKeyboard();
        if (pluginService.enabled().contains(plugin)) {
            keyboard.add(buildDisableButton(plugin));
        }else{
            keyboard.add(buildEnableButton(plugin));
        }
        markdown.keyboard(keyboard);
        context.reply(markdown);
    }

    private MessageKeyboardButton buildEnableButton(Plugin plugin) {
        return MessageKeyboardButton.command()
                .label("启用")
                .data("启用插件 %s".formatted(plugin.id()))
                .enter()
                .build();
    }

    private MessageKeyboardButton buildDisableButton(Plugin plugin) {
        return MessageKeyboardButton.command()
                .label("禁用")
                .data("禁用插件 %s".formatted(plugin.id()))
                .enter()
                .build();
    }

}
