package cc.rapidev.qqbot.boot.plugin.command;

import cc.rapidev.qqbot.boot.plugin.PluginService;
import cc.rapidev.qqbot.common.Events;
import cc.rapidev.qqbot.message.MessageDispatcher;
import cc.rapidev.qqbot.message.MessageHandler;
import cc.rapidev.qqbot.message.MessageHandlerInjector;

import java.util.ArrayList;
import java.util.List;

/**
 * @author leibrother
 */
public class PluginCommandInjector implements MessageHandlerInjector {

    private final List<MessageHandler> commands;

    public PluginCommandInjector(PluginService service) {
        commands = new ArrayList<>();
        commands.add(new PluginListCommand(service));
        commands.add(new PluginDetailCommand(service));
        commands.add(new PluginEnableCommand(service));
    }

    @Override
    public void inject(MessageDispatcher dispatcher) {
        dispatcher.register(Events.C2C_MESSAGE_CREATE, commands);
    }

}
