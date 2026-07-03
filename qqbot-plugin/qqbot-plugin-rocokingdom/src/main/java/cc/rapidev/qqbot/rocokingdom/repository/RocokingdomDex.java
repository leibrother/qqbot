package cc.rapidev.qqbot.rocokingdom.repository;

import cc.rapidev.qqbot.database.entity.annotations.DBTable;
import cc.rapidev.qqbot.database.entity.annotations.TBColumn;
import cc.rapidev.qqbot.database.entity.annotations.TBPrimaryKey;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author leibrother
 */
@Getter
@Setter
@NoArgsConstructor
@DBTable(name = "bot_rocokingdom_dex")
public class RocokingdomDex {

    @TBColumn
    @TBPrimaryKey
    private int id;
    @TBColumn
    private String no;
    @TBColumn
    private String name;
    @TBColumn
    private String form;
    @TBColumn
    private String fullname;
    @TBColumn
    private String desc;
    @TBColumn
    private String image;
    @TBColumn
    private String e1;
    @TBColumn
    private String e2;
    @TBColumn
    private String txname;
    @TBColumn
    private String txdesc;

    @TBColumn
    private int hp;
    @TBColumn
    private int patk;
    @TBColumn
    private int matk;
    @TBColumn
    private int pdf;
    @TBColumn
    private int mdf;
    @TBColumn
    private int spd;

}
