package cc.rapidev.qqbot.rocokingdom.model;

import java.util.List;

/**
 * @author leibrother
 */
public record Merchant(
        int round,
        long startTime,
        long endTime,
        List<MerchantItem> items
) {

    public record MerchantItem(String name, String iconUrl, int price, int buyLimitNum) {
    }

}
