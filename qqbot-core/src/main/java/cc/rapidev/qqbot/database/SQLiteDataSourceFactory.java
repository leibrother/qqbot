package cc.rapidev.qqbot.database;

import cc.rapidev.qqbot.common.utils.LogbackUtils;
import ch.qos.logback.classic.Level;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.nio.file.Path;

/**
 * @author leibrother
 */
public class SQLiteDataSourceFactory {

    static {
        LogbackUtils.setLogLevel("com.zaxxer.hikari", Level.INFO);
    }

    public static DataSource create(Path path, String database) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:sqlite:" + path.resolve(database + ".db"));
        config.setDriverClassName("org.sqlite.JDBC");
        config.setMinimumIdle(1);
        config.setMaximumPoolSize(1);
        config.setConnectionTestQuery("SELECT 1");
        return new HikariDataSource(config);
    }

}
