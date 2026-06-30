package cc.rapidev.qqbot.launcher.plugin.view;

import cc.rapidev.qqbot.common.markdown.MarkdownUI;
import cc.rapidev.qqbot.common.markdown.MarkdownView;
import cc.rapidev.qqbot.launcher.plugin.Plugin;

/**
 * @author leibrother
 */
public class PluginView extends MarkdownView {

    public PluginView(Plugin plugin) {
        add(MarkdownUI.h1(plugin.name()));
        add(
                MarkdownUI.block(
                        MarkdownUI.blockQuote(
                                MarkdownUI.bold("ID:"),
                                MarkdownUI.whitespace(),
                                MarkdownUI.text(plugin.id())
                        ),
                        MarkdownUI.blockQuote(
                                MarkdownUI.bold("版本:"),
                                MarkdownUI.whitespace(),
                                MarkdownUI.text(plugin.version().toString())
                        ),
                        MarkdownUI.blockQuote(
                                MarkdownUI.bold("作者:"),
                                MarkdownUI.whitespace(),
                                MarkdownUI.text(plugin.manifest().author())
                        )
                )
        );
        add(MarkdownUI.block(MarkdownUI.text(plugin.description())));
    }

}
