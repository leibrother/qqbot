package cc.rapidev.qqbot.message.view;

import cc.rapidev.qqbot.common.markdown.MarkdownUI;
import cc.rapidev.qqbot.common.markdown.MarkdownView;
import cc.rapidev.qqbot.common.utils.ExceptionUtils;

/**
 * @author leibrother
 */
public class ExceptionView extends MarkdownView<ExceptionView> {

    public ExceptionView(Exception exception) {
        String trace = ExceptionUtils.getStackTrace(exception);
        add(MarkdownUI.h1("错误"));
        add(MarkdownUI.blockQuote("机器人发生异常，请联系开发者"));
        add(MarkdownUI.separator());
        add(MarkdownUI.blockCode(trace));
    }

}
