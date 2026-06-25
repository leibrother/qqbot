package cc.rapidev.qqbot.extension.template;

import cc.rapidev.qqbot.api.model.Message;

import java.io.StringWriter;

/**
 * @author leibrother
 */
public class Rendered {

    private final StringWriter writer;

    public Rendered(StringWriter writer) {
        this.writer = writer;
    }

    @Override
    public String toString() {
        return writer.toString();
    }

    public Message text() {
        return Message.text(toString());
    }

    public Message markdown() {
        return Message.markdown(toString());
    }

}
