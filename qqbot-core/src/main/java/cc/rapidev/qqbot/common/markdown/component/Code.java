package cc.rapidev.qqbot.common.markdown.component;

import cc.rapidev.qqbot.common.utils.StringUtils;

/**
 * @author leibrother
 */
public class Code extends Text {

    public Code(String value) {
        super(value);
    }

    @Override
    public String render() {
        String text = super.render();
        return StringUtils.packing("`", text);
    }

}
