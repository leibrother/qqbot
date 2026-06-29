package cc.rapidev.qqbot.common.markdown.interactive;

import cc.rapidev.qqbot.common.markdown.component.Text;

/**
 * @author leibrother
 */
public class CmdEnter extends Text {

    public CmdEnter(String value) {
        super(value);
    }

    @Override
    public String render() {
        return "<qqbot-cmd-enter text=\"%s\" />".formatted(super.render());
    }

}
