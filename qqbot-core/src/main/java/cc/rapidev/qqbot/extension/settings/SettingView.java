package cc.rapidev.qqbot.extension.settings;

import cc.rapidev.qqbot.common.markdown.MarkdownUI;
import cc.rapidev.qqbot.common.markdown.MarkdownView;

/**
 * @author leibrother
 */
public class SettingView extends MarkdownView<SettingView> {

    public SettingView(Setting setting, RenderContext context) {
        add(MarkdownUI.h1("设置"));
        add(MarkdownUI.block(MarkdownUI.blockQuote(setting.description())));
        add(MarkdownUI.separator());
        add(setting.render(context));
        add(MarkdownUI.separator());
        if (setting.parent() != null) {
            add(MarkdownUI.block(MarkdownUI.cmdEnter("返回")));
        }
        add(MarkdownUI.block(MarkdownUI.cmdEnter("退出设置")));
    }

}
