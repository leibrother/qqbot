package cc.rapidev.qqbot.launcher.plugin.command;

import cc.rapidev.qqbot.common.Events;
import cc.rapidev.qqbot.extension.command.CommandHandlerSet;
import cc.rapidev.qqbot.extension.command.Keyword;
import cc.rapidev.qqbot.extension.command.KeywordRegister;
import cc.rapidev.qqbot.launcher.plugin.PluginService;

/**
 * @author leibrother
 */
public class PluginKeywordRegister extends KeywordRegister {

    private final PluginService pluginService;

    public PluginKeywordRegister(PluginService pluginService) {
        this.pluginService = pluginService;
    }

    @Override
    protected void register(CommandHandlerSet handler) {
        handler.add(new Keyword("插件列表"), new PluginListCommand(pluginService), Events.C2C_MESSAGE_CREATE);
        handler.add(new Keyword("插件详情"), new PluginDetailCommand(pluginService), Events.C2C_MESSAGE_CREATE);
        handler.add(new Keyword("启用插件"), new PluginEnableCommand(pluginService), Events.C2C_MESSAGE_CREATE);
        handler.add(new Keyword("禁用插件"), new PluginDisableCommand(pluginService), Events.C2C_MESSAGE_CREATE);
    }

}
