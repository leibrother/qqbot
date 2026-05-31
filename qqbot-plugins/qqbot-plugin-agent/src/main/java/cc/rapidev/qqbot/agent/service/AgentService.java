package cc.rapidev.qqbot.agent.service;

import cc.rapidev.qqbot.Bot;
import cc.rapidev.qqbot.agent.Agent;
import cc.rapidev.qqbot.agent.model.ModelFactory;
import cc.rapidev.qqbot.agent.model.ModelValues;
import cc.rapidev.qqbot.common.Topic;
import cc.rapidev.qqbot.message.MessageContext;
import io.agentscope.core.model.Model;
import io.agentscope.core.tool.Toolkit;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author leibrother
 */
public class AgentService implements AutoCloseable {

    private final Toolkit toolkit;
    private final AgentSettingService settings;
    private final Map<Topic, Agent> agents = new ConcurrentHashMap<>();

    public AgentService(Bot bot) {
        this.toolkit = new Toolkit();
        this.settings = new AgentSettingService(bot);
    }

    @Override
    public void close() {
        this.settings.close();
    }

    public void chat(MessageContext context) {
        ModelValues modelValues = settings.getModelValues(context);
        if (modelValues == null) {
            return;
        }
        Model model = ModelFactory.produce(modelValues);
    }

}
