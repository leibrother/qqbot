package cc.rapidev.qqbot.ai.tools;

import cc.rapidev.qqbot.message.MessageContext;
import dev.langchain4j.agent.tool.ToolSpecification;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author leibrother
 */
public class Today implements AiTool {

    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public ToolSpecification specification() {
        return ToolSpecification.builder()
                .name("today")
                .description("获取当前日期与时间，格式为ISO-8601")
                .build();
    }

    @Override
    public String invoke(MessageContext context, String arguments) {
        LocalDateTime now = LocalDateTime.now();
        return now.format(formatter);
    }

}
