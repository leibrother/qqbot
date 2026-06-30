package cc.rapidev.qqbot.launcher.plugin.view;

import cc.rapidev.qqbot.common.markdown.MarkdownUI;
import cc.rapidev.qqbot.common.markdown.MarkdownView;
import cc.rapidev.qqbot.common.markdown.component.Table;
import cc.rapidev.qqbot.launcher.plugin.Plugin;

import java.util.List;
import java.util.Map;

/**
 * @author leibrother
 */
public class PluginListView extends MarkdownView {

    public PluginListView(List<Plugin> plugins, Map<String, String> statuses) {
        add(MarkdownUI.h1("插件列表"));
        add(
                MarkdownUI.blockQuote(
                        MarkdownUI.text("发送"),
                        MarkdownUI.whitespace(),
                        MarkdownUI.cmdInput("插件详情", "插件详情 [ID]", false),
                        MarkdownUI.whitespace(),
                        MarkdownUI.text("查看插件详情")
                ),
                MarkdownUI.blockQuote(
                        MarkdownUI.text("发送"),
                        MarkdownUI.whitespace(),
                        MarkdownUI.cmdInput("启用插件", "启用插件 [ID]", false),
                        MarkdownUI.whitespace(),
                        MarkdownUI.text("启用插件")
                ),
                MarkdownUI.blockQuote(
                        MarkdownUI.text("发送"),
                        MarkdownUI.whitespace(),
                        MarkdownUI.cmdInput("禁用插件", "禁用插件 [ID]", false),
                        MarkdownUI.whitespace(),
                        MarkdownUI.text("禁用插件")
                )
        );
        add(MarkdownUI.separator());

        Table.TableBodyBuilder body = Table.builder()
                .header()
                .head(MarkdownUI.text("ID"))
                .head(MarkdownUI.text("名称"))
                .head(MarkdownUI.text("版本"))
                .head(MarkdownUI.text("状态"))
                .body();
        for (Plugin plugin : plugins) {
            body.row(
                    List.of(
                            MarkdownUI.block(MarkdownUI.cmdInput("插件详情 " + plugin.id(), plugin.id(), false)),
                            MarkdownUI.block(MarkdownUI.text(plugin.name())),
                            MarkdownUI.block(MarkdownUI.text(plugin.version().toString())),
                            MarkdownUI.block(MarkdownUI.text(statuses.get(plugin.id())))
                    )
            );
        }
        add(body.build());
    }

}
