package cc.rapidev.qqbot.rocokingdom.view;

import cc.rapidev.qqbot.common.markdown.MarkdownUI;
import cc.rapidev.qqbot.common.markdown.MarkdownView;
import cc.rapidev.qqbot.common.markdown.component.Listview;
import com.fasterxml.jackson.databind.JsonNode;

import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author leibrother
 */
public class FarawayMerchantView extends MarkdownView<FarawayMerchantView> {

    private static final List<String> numbers = List.of("零", "一", "二", "三", "四");
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DecimalFormat amountFormatter = new DecimalFormat("#,###");

    public FarawayMerchantView(JsonNode data) {
        int round = data.get("round").intValue();
        LocalDateTime endtime = Instant.ofEpochMilli(data.get("endTime").longValue()).atZone(ZoneOffset.of("+8")).toLocalDateTime();

        add(MarkdownUI.h1("远行商人"));
        add(
                MarkdownUI.blockQuote(
                        MarkdownUI.bold("当前轮次:"),
                        MarkdownUI.whitespace(),
                        MarkdownUI.text("第%s轮".formatted(numbers.get(round)))
                ),
                MarkdownUI.blockQuote(
                        MarkdownUI.bold("截止时间:"),
                        MarkdownUI.whitespace(),
                        MarkdownUI.text(timeFormatter.format(endtime))
                )
        );
        add(MarkdownUI.separator());

        Listview list = MarkdownUI.list();
        add(list);
        data.get("items")
                .elements()
                .forEachRemaining(item -> {
                    String name = item.get("name").textValue();
                    int buyLimitNum = item.get("buyLimitNum").intValue();
                    int price = item.get("price").intValue();
                    list.add(
                            MarkdownUI.item(MarkdownUI.bold(name)),
                            MarkdownUI.block(
                                    MarkdownUI.blockQuote(
                                            MarkdownUI.bold("限购:"),
                                            MarkdownUI.whitespace(),
                                            MarkdownUI.text(String.valueOf(buyLimitNum))
                                    ),
                                    MarkdownUI.blockQuote(
                                            MarkdownUI.bold("价格:"),
                                            MarkdownUI.whitespace(),
                                            MarkdownUI.text(amountFormatter.format(price))
                                    )
                            )
                    );
                });
    }


}
