package cc.rapidev.qqbot.common.markdown.interactive;

import cc.rapidev.qqbot.common.markdown.component.Text;
import cc.rapidev.qqbot.common.utils.StringUtils;

/**
 * @author leibrother
 */
public class CmdInput extends Text {

    private final String show;
    private final boolean reference;

    public CmdInput(String text) {
        this(text, null, false);
    }

    public CmdInput(String text, String show, boolean reference) {
        super(text);
        this.show = show;
        this.reference = reference;
    }

    @Override
    public String render() {
        String text = super.render();
        StringBuilder builder = new StringBuilder();
        builder.append("<qqbot-cmd-input");
        builder.append(" text=\"").append(text).append("\"");
        if (StringUtils.isNotEmpty(this.show)) {
            builder.append(" show=\"").append(this.show).append("\"");
        }
        if (this.reference) {
            builder.append(" reference=\"true\"");
        }
        builder.append(" />");
        return builder.toString();
    }

}
