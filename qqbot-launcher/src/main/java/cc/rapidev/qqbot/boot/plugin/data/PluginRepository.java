package cc.rapidev.qqbot.boot.plugin.data;

import cc.rapidev.qqbot.database.BotDatabase;

/**
 * @author leibrother
 */
public class PluginRepository {

    private final BotDatabase database;

    public PluginRepository(BotDatabase database) {
        this.database = database;
        this.init();
    }

    private void init() {
        if (database.exist("plugin_enabled")) {
            return;
        }
        database.execute("""
                CREATE TABLE IF NOT EXISTS plugin_enabled (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name varchar(255),
                
                )
                """);
    }

    public void isEnabled(String name) {

    }

}
