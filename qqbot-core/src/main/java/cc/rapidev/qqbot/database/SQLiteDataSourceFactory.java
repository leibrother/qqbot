package cc.rapidev.qqbot.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

/**
 * @author leibrother
 */
public class SQLiteDataSourceFactory {

    public static DataSource create(String database) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:sqlite:" + database);
        config.setDriverClassName("org.sqlite.JDBC");
        config.setMinimumIdle(1);
        config.setMaximumPoolSize(1);
        config.setConnectionTestQuery("SELECT 1");
        return new HikariDataSource(config);
    }

}
