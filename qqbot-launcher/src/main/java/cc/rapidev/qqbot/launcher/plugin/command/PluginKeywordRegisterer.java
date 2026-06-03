package cc.rapidev.qqbot.launcher.plugin.command;

import cc.rapidev.qqbot.common.Event;
import cc.rapidev.qqbot.extension.command.CommandHandlerSet;
import cc.rapidev.qqbot.extension.command.Keyword;
import cc.rapidev.qqbot.extension.command.KeywordRegisterer;
import cc.rapidev.qqbot.launcher.plugin.PluginService;

/**
 * @author leibrother
 */
public class PluginKeywordRegisterer implements KeywordRegisterer {

    private final PluginService pluginService;

    public PluginKeywordRegisterer(PluginService pluginService) {
        this.pluginService = pluginService;
    }

    @Override
    public void register(CommandHandlerSet set) {
        set.add(new Keyword("插件列表"), new PluginListCommand(pluginService), Event.C2C_MESSAGE_CREATE);
        set.add(new Keyword("插件详情"), new PluginDetailCommand(pluginService), Event.C2C_MESSAGE_CREATE);
        set.add(new Keyword("启用插件"), new PluginEnableCommand(pluginService), Event.C2C_MESSAGE_CREATE);
        set.add(new Keyword("禁用插件"), new PluginDisableCommand(pluginService), Event.C2C_MESSAGE_CREATE);
    }

}
