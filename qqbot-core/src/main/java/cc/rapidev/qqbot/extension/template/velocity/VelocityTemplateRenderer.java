package cc.rapidev.qqbot.extension.template.velocity;

import cc.rapidev.qqbot.common.utils.LogbackUtils;
import cc.rapidev.qqbot.extension.template.TemplateRenderer;
import ch.qos.logback.classic.Level;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author leibrother
 */
public class VelocityTemplateRenderer implements TemplateRenderer {

    private final VelocityEngine engine;
    private final Map<String, Template> resources = new HashMap<>();

    public VelocityTemplateRenderer() {
        this.engine = new VelocityEngine();
        LogbackUtils.setLogLevel("org.apache.velocity", Level.INFO);
        engine.setProperty(VelocityEngine.RESOURCE_LOADERS, "classpath");
        engine.setProperty("resource.loader.classpath.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        engine.init();
    }

    private Template load(String name) {
        if (resources.containsKey(name)) {
            return resources.get(name);
        }
        Template template = engine.getTemplate(name, StandardCharsets.UTF_8.name());
        resources.put(name, template);
        return template;
    }

    @Override
    public StringWriter render(String name, Map<String, Object> ctx) {
        Template template = this.load(name);
        VelocityContext context = new VelocityContext(ctx);
        StringWriter writer = new StringWriter();
        template.merge(context, writer);
        return writer;
    }

    @Override
    public void close() {
        this.resources.clear();
    }

}
