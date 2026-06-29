package cc.rapidev.qqbot.common.markdown.component;

import cc.rapidev.qqbot.common.utils.StringUtils;

/**
 * @author leibrother
 */
public class Bold extends Text {

    public Bold(Text text) {
        this(text.render());
    }

    public Bold(String value) {
        super(value);
    }

    @Override
    public String render() {
        return StringUtils.packing("**", value);
    }

}
