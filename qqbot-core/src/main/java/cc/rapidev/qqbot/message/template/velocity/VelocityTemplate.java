package cc.rapidev.qqbot.message.template.velocity;

import cc.rapidev.qqbot.message.template.TemplateResource;
import org.apache.velocity.Template;

/**
 * @author leibrother
 */
public record VelocityTemplate(Template resource) implements TemplateResource {
}
