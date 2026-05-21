package cc.rapidev.qqbot.extension.command;

import cc.rapidev.qqbot.Bot;
import cc.rapidev.qqbot.common.Events;
import cc.rapidev.qqbot.extension.Extension;
import cc.rapidev.qqbot.message.MessageContext;
import cc.rapidev.qqbot.message.MessageDispatcher;
import cc.rapidev.qqbot.message.MessageHandler;

/**
 * @author leibrother
 */
public class CommandHandlerExtension implements Extension {

    private final MessageDispatcher dispatcher;
    private final CommandEntry entry;

    public CommandHandlerExtension(Bot bot) {
        this.dispatcher = bot.dispatcher();
        this.entry = new CommandEntry();
        this.registerEntry();
        this.registerEnterService();
    }

    /**
     * 注入指令处理器入口
     */
    private void registerEntry() {
        Events.messageCreateEvents.forEach(e -> dispatcher.register(e, entry));
    }

    /**
     * 启动完成时向上下文注入指令处理器入口
     */
    private void registerEnterService() {
        this.dispatcher.register(Events.STARTED, new MessageHandler() {
            @Override
            public int order() {
                return MessageHandler.super.order() - 101;
            }

            @Override
            public void handle(MessageContext context) {
                context.addService(entry);
            }
        });
    }

    @Override
    public void destroy() {
    }

}
