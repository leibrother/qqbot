package cc.rapidev.qqbot.common.markdown.component;

import cc.rapidev.qqbot.common.utils.StringUtils;

/**
 * @author leibrother
 */
public class Italic extends Text {

    public Italic(Text text) {
        this(text.render());
    }

    public Italic(String value) {
        super(value);
    }

    @Override
    public String render() {
        return StringUtils.packing("*", value);
    }

}
