package cc.rapidev.qqbot.extension.template;

import cc.rapidev.qqbot.Bot;
import cc.rapidev.qqbot.common.Events;
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

    private final TemplateRenderer templateRenderer;

    public TemplateExtension(Bot bot) {
        this.templateRenderer = new VelocityTemplateRenderer();
        MessageDispatcher dispatcher = bot.dispatcher();
        Arrays.stream(Events.values()).forEach(event -> dispatcher.register(event, this));
    }

    @Override
    public int order() {
        return 0;
    }

    @Override
    public void handle(MessageContext context) {
        context.addService(templateRenderer);
    }

    @Override
    public void destroy() throws Exception {
        this.templateRenderer.close();
    }

}
