package cc.rapidev.qqbot.extension.template;

import cc.rapidev.qqbot.Bot;
import cc.rapidev.qqbot.common.Event;
import cc.rapidev.qqbot.extension.Extension;
import cc.rapidev.qqbot.extension.template.velocity.VelocityTemplateRenderer;
import cc.rapidev.qqbot.message.MessageContext;
import cc.rapidev.qqbot.message.MessageDispatcher;
import cc.rapidev.qqbot.message.MessageHandler;

import java.util.Arrays;

/**
 * @author leibrother
 */
public class TemplateExtension implements Extension, MessageHandler {

    private TemplateRenderer renderer;

    @Override
    public int order() {
        return 0;
    }

    @Override
    public void handle(MessageContext context) {
        context.add(renderer);
    }

    @Override
    public void ready(Bot bot) {
        this.renderer = new VelocityTemplateRenderer();
        bot.add(renderer);
        MessageDispatcher dispatcher = bot.dispatcher();
        Arrays.stream(Event.values()).forEach(event -> dispatcher.register(event, this));
    }

    @Override
    public void destroy() throws Exception {
        this.renderer.close();
    }

}
