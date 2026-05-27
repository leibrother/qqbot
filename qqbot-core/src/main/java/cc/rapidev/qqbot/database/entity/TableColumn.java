package cc.rapidev.qqbot.database.entity;

import cc.rapidev.qqbot.common.utils.StringUtils;

import java.util.Objects;

/**
 * @author leibrother
 */
public record TableColumn(
        String name,
        String type,
        boolean notnull,
        String defaultValue,
        boolean primaryKey
) {

    public String safename(){
        return StringUtils.packing("'", name);
    }

    public String schema() {
        StringBuilder builder = new StringBuilder();
        // name
        builder.append(safename());
        // type
        builder.append(" ").append(type);
        // notnull
        if (notnull) {
            builder.append(" NOT NULL");
        }
        // default value
        if (defaultValue != null && !defaultValue.isEmpty()) {
            builder.append(" DEFAULT ").append(defaultValue);
        }
        return builder.toString();
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        TableColumn that = (TableColumn) object;
        return notnull == that.notnull
                && primaryKey == that.primaryKey
                && Objects.equals(name, that.name)
                && Objects.equals(type, that.type)
                && Objects.equals(defaultValue, that.defaultValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, notnull, defaultValue, primaryKey);
    }
}
