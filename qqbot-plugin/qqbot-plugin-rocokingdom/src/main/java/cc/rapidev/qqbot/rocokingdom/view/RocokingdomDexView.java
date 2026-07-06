package cc.rapidev.qqbot.rocokingdom.view;

import cc.rapidev.qqbot.common.markdown.MarkdownUI;
import cc.rapidev.qqbot.common.markdown.MarkdownView;
import cc.rapidev.qqbot.common.markdown.component.Table;
import cc.rapidev.qqbot.common.utils.StringUtils;
import cc.rapidev.qqbot.rocokingdom.repository.entity.RocokingdomDex;

import java.util.List;

/**
 * @author leibrother
 */
public class RocokingdomDexView extends MarkdownView {

    public RocokingdomDexView(RocokingdomDex dex) {
        add(MarkdownUI.h1(
                MarkdownUI.text(dex.getNo()), MarkdownUI.whitespace(), MarkdownUI.text(dex.getName())
        ));
        add(MarkdownUI.blockQuote(
                MarkdownUI.text("属性:"),
                MarkdownUI.whitespace(),
                StringUtils.isEmpty(dex.getE2()) ? MarkdownUI.text(dex.getE1()) : MarkdownUI.text(dex.getE1() + " " + dex.getE2())
        ));
        if (StringUtils.isNotEmpty(dex.getForm())) {
            add(MarkdownUI.blockQuote(
                    MarkdownUI.text("形态:"),
                    MarkdownUI.whitespace(),
                    MarkdownUI.text(dex.getForm())
            ));
        }
        add(MarkdownUI.blockQuote(MarkdownUI.text(dex.getDesc())));
        add(MarkdownUI.separator());
        add(MarkdownUI.block(
                MarkdownUI.block(MarkdownUI.bold(dex.getTxname())),
                MarkdownUI.blockQuote(MarkdownUI.text(dex.getTxdesc()))
        ));
        add(MarkdownUI.separator());
        add(MarkdownUI.block(
                MarkdownUI.block(MarkdownUI.bold("基础属性")),
                Table.builder()
                        .header()
                        .head(MarkdownUI.text("属性"))
                        .head(MarkdownUI.block(MarkdownUI.text("值")), Table.Align.RIGHT)
                        .body()
                        .row(List.of(MarkdownUI.block(MarkdownUI.text("生命")), MarkdownUI.block(MarkdownUI.text(String.valueOf(dex.getHp())))))
                        .row(List.of(MarkdownUI.block(MarkdownUI.text("物攻")), MarkdownUI.block(MarkdownUI.text(String.valueOf(dex.getPatk())))))
                        .row(List.of(MarkdownUI.block(MarkdownUI.text("魔攻")), MarkdownUI.block(MarkdownUI.text(String.valueOf(dex.getMatk())))))
                        .row(List.of(MarkdownUI.block(MarkdownUI.text("物防")), MarkdownUI.block(MarkdownUI.text(String.valueOf(dex.getPdf())))))
                        .row(List.of(MarkdownUI.block(MarkdownUI.text("魔防")), MarkdownUI.block(MarkdownUI.text(String.valueOf(dex.getMdf())))))
                        .row(List.of(MarkdownUI.block(MarkdownUI.text("速度")), MarkdownUI.block(MarkdownUI.text(String.valueOf(dex.getSpd())))))
                        .build()
        ));
    }

}
