package cc.rapidev.qqbot.extension.command.view;

import cc.rapidev.qqbot.common.markdown.MarkdownUI;
import cc.rapidev.qqbot.common.markdown.MarkdownView;
import cc.rapidev.qqbot.common.utils.StringUtils;
import cc.rapidev.qqbot.extension.command.Keyword;

import java.util.List;

/**
 * @author leibrother
 */
public class HelpView extends MarkdownView<HelpView> {

    public HelpView(List<Keyword> keywords) {
        add(MarkdownUI.h1("帮助"));
        add(MarkdownUI.blockQuote("可用指令列表"));
        add(MarkdownUI.separator());
        if (keywords == null || keywords.isEmpty()) {
            add(MarkdownUI.block(MarkdownUI.text("无可用指令")));
        } else {
            for (Keyword keyword : keywords) {
                add(
                        MarkdownUI.block(
                                MarkdownUI.block(MarkdownUI.bold(keyword.key())),
                                MarkdownUI.blockQuote(StringUtils.orDefault(keyword.description(), "暂无说明"))
                        )
                );
            }
        }

    }

}
