package cc.rapidev.qqbot.rocokingdom.repository.entity;

import cc.rapidev.qqbot.database.entity.annotations.DBTable;
import cc.rapidev.qqbot.database.entity.annotations.TBColumn;
import cc.rapidev.qqbot.database.entity.annotations.TBPrimaryKey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author leibrother
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DBTable(name = "rocokingdom_merchant_push_log")
public class MerchantPushLog {

    @TBColumn
    @TBPrimaryKey
    private String date;
    @TBColumn
    @TBPrimaryKey
    private int round;

}
