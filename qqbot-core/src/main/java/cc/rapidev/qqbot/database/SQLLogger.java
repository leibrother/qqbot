package cc.rapidev.qqbot.database;

import org.jdbi.v3.core.statement.SqlLogger;
import org.jdbi.v3.core.statement.StatementContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author leibrother
 */
public class SQLLogger implements SqlLogger {

    private final Logger logger = LoggerFactory.getLogger("cc.rapidev.qqbot.database");

    public void logAfterExecution(StatementContext context) {
        if (this.logger.isDebugEnabled()) {
            String sql = context.getParsedSql().getSql().replace("\n", " ").replace("\r", " ");
            this.logger.debug(sql);
        }
    }

}
