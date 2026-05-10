package cc.rapidev.qqbot.message.template.velocity;

import cc.rapidev.qqbot.message.template.TemplateService;
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
public class VelocityTemplateService implements TemplateService<VelocityTemplate> {

    private final VelocityEngine engine;
    private final Map<String, VelocityTemplate> resources = new HashMap<>();

    public VelocityTemplateService() {
        this.engine = new VelocityEngine();
        engine.setProperty(VelocityEngine.RESOURCE_LOADERS, "classpath");
        engine.setProperty("resource.loader.classpath.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        engine.init();
    }

    @Override
    public VelocityTemplate load(String name) {
        if (resources.containsKey(name)) {
            return resources.get(name);
        }
        Template template = engine.getTemplate(name, StandardCharsets.UTF_8.name());
        VelocityTemplate resource = new VelocityTemplate(template);
        resources.put(name, resource);
        return resource;
    }

    @Override
    public StringWriter render(VelocityTemplate template, Map<String, Object> ctx) {
        Template resource = template.resource();
        VelocityContext context = new VelocityContext(ctx);
        StringWriter writer = new StringWriter();
        resource.merge(context, writer);
        return writer;
    }

}
