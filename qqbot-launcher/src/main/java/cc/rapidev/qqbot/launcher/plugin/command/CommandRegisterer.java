package cc.rapidev.qqbot.launcher.plugin.command;

import cc.rapidev.qqbot.common.Event;
import cc.rapidev.qqbot.extension.command.CommandHandlerSet;
import cc.rapidev.qqbot.extension.command.Keyword;
import cc.rapidev.qqbot.extension.command.KeywordRegisterer;
import cc.rapidev.qqbot.launcher.plugin.PluginService;

/**
 * @author leibrother
 */
public class CommandRegisterer implements KeywordRegisterer {

    private final PluginService pluginService;

    public CommandRegisterer(PluginService pluginService) {
        this.pluginService = pluginService;
    }

    @Override
    public void register(CommandHandlerSet set) {
        Event[] events = new Event[]{Event.C2C_MESSAGE_CREATE};
        set.add(new Keyword("插件列表", "查看插件列表"), new PluginListHandler(pluginService), events);
        set.add(new Keyword("插件详情", "查看插件详情"), new PluginDetailHandler(pluginService), events);
        set.add(new Keyword("启用插件", "启用指定的插件"), new PluginEnableHandler(pluginService), events);
        set.add(new Keyword("禁用插件", "禁用指定的插件"), new PluginDisableHandler(pluginService), events);
    }

}
