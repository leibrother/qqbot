package cc.rapidev.qqbot.extension.template;

import java.io.Closeable;
import java.util.Map;

/**
 * @author leibrother
 */
public interface TemplateRenderer extends Closeable {

    Rendered render(String name, Map<String, Object> params);

}
