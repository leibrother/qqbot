package cc.rapidev.qqbot.launcher.plugin.view;

import cc.rapidev.qqbot.common.markdown.MarkdownUI;
import cc.rapidev.qqbot.common.markdown.MarkdownView;
import cc.rapidev.qqbot.common.markdown.component.Table;
import cc.rapidev.qqbot.launcher.plugin.Plugin;

import java.util.List;

/**
 * @author leibrother
 */
public class PluginEnableResultView extends MarkdownView {

    public PluginEnableResultView(Plugin plugin, List<Plugin> dependencies, List<Plugin> enabled) {
        add(MarkdownUI.h1("结果"));
        add(MarkdownUI.blockQuote("插件启用结果"));
        add(MarkdownUI.separator());
        add(MarkdownUI.block(MarkdownUI.text(plugin.name()), MarkdownUI.whitespace(), MarkdownUI.text("已启用")));
        if (dependencies != null && !dependencies.isEmpty()) {
            add(MarkdownUI.separator());
            add(MarkdownUI.block(MarkdownUI.bold("同时启用了以下依赖插件")));
            Table.TableBodyBuilder body = Table.builder()
                    .header()
                    .head(MarkdownUI.text("ID"))
                    .head(MarkdownUI.text("名称"))
                    .head(MarkdownUI.text("说明"))
                    .body();
            for (Plugin dependency : dependencies) {
                body.row(
                        List.of(
                                MarkdownUI.block(MarkdownUI.cmdInput("插件详情 " + dependency.id(), dependency.id(), false)),
                                MarkdownUI.block(MarkdownUI.text(dependency.name())),
                                MarkdownUI.block(MarkdownUI.text(dependency.description()))
                        )
                );
            }
            add(body.build());
        }
        add(MarkdownUI.separator());
        add(
                MarkdownUI.blockQuote(
                        MarkdownUI.text("当前已启用%s个插件".formatted(enabled.size())),
                        MarkdownUI.whitespace(),
                        MarkdownUI.cmdInput("插件列表 已启用", "查看详情", false)
                )
        );
    }

}
