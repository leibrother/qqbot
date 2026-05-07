package cc.rapidev.qqbot.message.template;

import cc.rapidev.qqbot.common.Events;
import cc.rapidev.qqbot.message.MessageContext;
import cc.rapidev.qqbot.message.MessageDispatcher;
import cc.rapidev.qqbot.message.MessageHandler;
import cc.rapidev.qqbot.message.MessageHandlerInjector;
import cc.rapidev.qqbot.message.template.velocity.VelocityTemplateService;

import java.util.Arrays;

/**
 * @author leibrother
 */
public class TemplateServiceInjector implements MessageHandler, MessageHandlerInjector {

    private final TemplateService<?> service;

    public TemplateServiceInjector() {
        this.service = new VelocityTemplateService();
    }

    @Override
    public void handle(MessageContext context) {
        context.addService("templateService", service);
    }

    @Override
    public void inject(MessageDispatcher dispatcher) {
        Arrays.stream(Events.values()).forEach(event -> dispatcher.register(event, this));
    }

}
