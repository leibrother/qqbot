package cc.rapidev.qqbot.extension.settings;

import cc.rapidev.qqbot.common.markdown.MarkdownUI;
import cc.rapidev.qqbot.common.markdown.MarkdownView;
import cc.rapidev.qqbot.message.MessageContext;

/**
 * @author leibrother
 */
public class SettingView extends MarkdownView {

    public SettingView(Setting setting, MessageContext context) {
        add(MarkdownUI.h1("设置"));
        add(MarkdownUI.blockQuote(setting.description()));
        add(MarkdownUI.separator());
        add(setting.render(context));
        add(MarkdownUI.separator());
        if (setting.parent() != null) {
            add(MarkdownUI.block(MarkdownUI.cmd(context.topic(), "返回")));
        }
        add(MarkdownUI.block(MarkdownUI.cmd(context.topic(), "退出设置")));
    }

}
