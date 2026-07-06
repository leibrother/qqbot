package cc.rapidev.qqbot.rocokingdom.view;

import cc.rapidev.qqbot.common.markdown.MarkdownUI;
import cc.rapidev.qqbot.common.markdown.MarkdownView;
import cc.rapidev.qqbot.common.markdown.component.Listview;
import cc.rapidev.qqbot.rocokingdom.model.Merchant;

import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author leibrother
 */
public class MerchantView extends MarkdownView {

    private static final List<String> numbers = List.of("零", "一", "二", "三", "四");
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DecimalFormat amountFormatter = new DecimalFormat("#,###");

    public MerchantView(Merchant merchant) {
        LocalDateTime endtime = Instant.ofEpochMilli(merchant.endTime()).atZone(ZoneOffset.of("+8")).toLocalDateTime();

        add(MarkdownUI.h1("远行商人"));
        add(
                MarkdownUI.blockQuote(
                        MarkdownUI.bold("当前轮次:"),
                        MarkdownUI.whitespace(),
                        MarkdownUI.text("第%s轮".formatted(numbers.get(merchant.round())))
                ),
                MarkdownUI.blockQuote(
                        MarkdownUI.bold("截止时间:"),
                        MarkdownUI.whitespace(),
                        MarkdownUI.text(timeFormatter.format(endtime))
                )
        );
        add(MarkdownUI.separator());

        Listview list = MarkdownUI.list();
        for (Merchant.MerchantItem item : merchant.items()) {
            list.add(
                    MarkdownUI.item(MarkdownUI.bold(item.name())),
                    MarkdownUI.block(
                            MarkdownUI.blockQuote(
                                    MarkdownUI.bold("限购:"),
                                    MarkdownUI.whitespace(),
                                    MarkdownUI.text(String.valueOf(item.buyLimitNum()))
                            ),
                            MarkdownUI.blockQuote(
                                    MarkdownUI.bold("价格:"),
                                    MarkdownUI.whitespace(),
                                    MarkdownUI.text(amountFormatter.format(item.price()))
                            )
                    )
            );
        }
        add(list);
    }


}
