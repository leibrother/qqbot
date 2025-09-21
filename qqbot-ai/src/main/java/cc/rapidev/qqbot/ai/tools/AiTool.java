package cc.rapidev.qqbot.ai.tools;

import dev.langchain4j.agent.tool.ToolSpecification;

/**
 * @author leibrother
 */
public interface AiTool {

    ToolSpecification specification();

    String invoke(String arguments);

}
