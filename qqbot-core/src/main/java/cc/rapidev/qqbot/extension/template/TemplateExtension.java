package cc.rapidev.qqbot.extension.template;

import cc.rapidev.qqbot.Bot;
import cc.rapidev.qqbot.extension.Extension;
import cc.rapidev.qqbot.extension.template.velocity.VelocityTemplateRenderer;

/**
 * @author leibrother
 */
public class TemplateExtension implements Extension {

    @Override
    public void ready(Bot bot) {
        bot.add(new VelocityTemplateRenderer());
    }

    @Override
    public void destroy() throws Exception {
    }

}
