package cc.rapidev.qqbot.agent;

import io.agentscope.core.ReActAgent;
import io.agentscope.core.model.Model;
import io.agentscope.core.tool.Toolkit;

/**
 * @author leibrother
 */
public class Agent {

    private final Model model;
    private final Toolkit toolkit;
    private final ReActAgent agent;

    public Agent(Model model) {
        this.model = model;
        this.toolkit = new Toolkit();
        this.agent = ReActAgent.builder()
                .model(model)
                .toolkit(toolkit)
                .build();
    }

}
