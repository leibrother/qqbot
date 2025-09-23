package cc.rapidev.qqbot.ai.tools;

import cc.rapidev.qqbot.message.MessageContext;
import dev.langchain4j.agent.tool.ToolSpecification;

/**
 * @author leibrother
 */
public interface AiTool {

    ToolSpecification specification();

    String invoke(MessageContext context, String arguments);

}
