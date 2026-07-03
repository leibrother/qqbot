package cc.rapidev.qqbot.database.entity;

import lombok.Getter;

import java.util.function.Function;

/**
 * @author leibrother
 */
public enum SQL {

    COUNT(Table::generateCountSQL),
    SELECT(Table::generateSelectSQL),
    INSERT(Table::generateInsertSQL),
    EXISTS_BY_PRIMARY_KEYS(Table::generateExistsByPrimaryKeysSQL),
    UPDATE_BY_PRIMARY_KEYS(Table::generateUpdateByPrimaryKeysSQL),
    DELETE_BY_PRIMARY_KEYS(Table::generateDeleteByPrimaryKeysSQL)
    ;

    @Getter
    private final Function<Table, String> generator;

    SQL(Function<Table, String> generator) {
        this.generator = generator;
    }

}
