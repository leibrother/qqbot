package cc.rapidev.qqbot.plugin.aigc.tools;

import cc.rapidev.qqbot.message.MessageContext;
import dev.langchain4j.agent.tool.ToolSpecification;

/**
 * @author leibrother
 */
public interface AiTool {

    ToolSpecification specification();

    String invoke(MessageContext context, String arguments);

}
