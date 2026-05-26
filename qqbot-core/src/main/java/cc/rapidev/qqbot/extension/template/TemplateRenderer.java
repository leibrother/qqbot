package cc.rapidev.qqbot.extension.template;

import cc.rapidev.qqbot.api.model.Message;

import java.io.Closeable;
import java.io.StringWriter;
import java.util.Map;

/**
 * @author leibrother
 */
public interface TemplateRenderer extends Closeable {

    StringWriter render(String name, Map<String, Object> params);

    default Message text(String name, Map<String, Object> params) {
        StringWriter writer = render(name, params);
        String content = writer.toString();
        return Message.text(content);
    }

    default Message markdown(String name, Map<String, Object> params) {
        StringWriter writer = render(name, params);
        String content = writer.toString();
        return Message.markdown(content);
    }

}
