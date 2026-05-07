package cc.rapidev.qqbot.message.template;

import cc.rapidev.qqbot.api.model.Message;

import java.io.StringWriter;
import java.util.Map;

/**
 * @author leibrother
 */
public interface TemplateService<T extends TemplateResource> {

    T load(String name);

    StringWriter render(T template, Map<String, Object> params);

    default StringWriter render(String name, Map<String, Object> params) {
        T template = load(name);
        return render(template, params);
    }

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
