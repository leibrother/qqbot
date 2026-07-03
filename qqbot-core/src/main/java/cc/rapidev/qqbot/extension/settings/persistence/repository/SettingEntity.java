package cc.rapidev.qqbot.extension.settings.persistence.repository;

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
@DBTable(name = "bot_settings")
public class SettingEntity {

    @TBColumn(notnull = true)
    @TBPrimaryKey
    private String key;

    @TBColumn(notnull = true)
    @TBPrimaryKey
    private String scope;

    @TBColumn
    private String value;

}
