package cc.rapidev.qqbot.extension.push.repository;

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
@DBTable(name = "bot_push_open")
public class PushOpenEntity {

    @TBColumn
    @TBPrimaryKey
    private String topic;

}
