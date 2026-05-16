package cc.rapidev.qqbot.boot.plugin.command;

import cc.rapidev.qqbot.boot.plugin.PluginService;
import cc.rapidev.qqbot.common.Events;
import cc.rapidev.qqbot.message.command.HierarchyCommandHandler;
import cc.rapidev.qqbot.message.command.Keyword;
import cc.rapidev.qqbot.message.command.KeywordRegister;

/**
 * @author leibrother
 */
public class PluginKeywordRegister extends KeywordRegister {

    private final PluginService pluginService;

    public PluginKeywordRegister(PluginService pluginService) {
        this.pluginService = pluginService;
    }

    @Override
    protected void register(HierarchyCommandHandler handler) {
        handler.register(new Keyword("插件列表", Events.C2C_MESSAGE_CREATE), new PluginListCommand(pluginService));
        handler.register(new Keyword("插件详情", Events.C2C_MESSAGE_CREATE), new PluginDetailCommand(pluginService));
        handler.register(new Keyword("启用插件", Events.C2C_MESSAGE_CREATE), new PluginEnableCommand(pluginService));
    }

}
