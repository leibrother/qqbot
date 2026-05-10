package cc.rapidev.qqbot.database;

import cc.rapidev.qqbot.Bot;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultIterable;
import org.jdbi.v3.sqlite3.SQLitePlugin;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import javax.sql.DataSource;
import java.io.File;

/**
 * @author leibrother
 */
public class BotDatabase {

    private final Jdbi jdbi;

    public BotDatabase(Bot bot) {
        String appid = bot.getConfig().getAppid();
        File directory = new File("./data");
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                throw new IllegalStateException("Unable to create database directory");
            }
        }
        String database = "./data/%s.db".formatted(appid);
        DataSource dataSource = SQLiteDataSourceFactory.create(database);
        this.jdbi = Jdbi.create(dataSource)
                .installPlugin(new SQLitePlugin())
                .installPlugin(new SqlObjectPlugin());
    }

    public int execute(String sql, Object... args) {
        return jdbi.withHandle(handle -> handle.execute(sql, args));
    }

    public void query() {
        try (Handle handle = jdbi.open()) {
            ResultIterable<Integer> integers = handle.createQuery("").mapTo(Integer.class);

        }
    }

    public boolean exist(String name) {
        return execute("SELECT 1 FROM sqlite_master WHERE type='table' AND name = ?", name) == 1;
    }

    public void repo(Class<? extends Repository> repository) {
        jdbi.useExtension(repository, (repo) -> {

        });
    }

}
