package cc.rapidev.qqbot.agent.tool;

import io.agentscope.core.tool.Tool;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author leibrother
 */
public class SimpleTools {

    @Tool(name = "get_time", description = "获取当前时间")
    public String now() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

}
