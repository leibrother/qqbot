package cc.rapidev.qqbot.common.markdown.component;

import cc.rapidev.qqbot.common.utils.StringUtils;

/**
 * @author leibrother
 */
public class EmphasizedText extends Text {

    private boolean bold = false;
    private boolean italic = false;

    public EmphasizedText(String value) {
        super(value);
    }

    public EmphasizedText bold(boolean bold) {
        this.bold = bold;
        return this;
    }

    public EmphasizedText italic(boolean italic) {
        this.italic = italic;
        return this;
    }

    @Override
    public String render() {
        String text = super.render();
        if (bold) {
            text = StringUtils.packing("**", text);
        }
        if (italic) {
            text = StringUtils.packing("*", text);
        }
        return text;
    }
}
