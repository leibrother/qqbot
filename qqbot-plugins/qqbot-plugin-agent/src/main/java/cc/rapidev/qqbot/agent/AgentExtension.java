package cc.rapidev.qqbot.agent;

import cc.rapidev.qqbot.Bot;
import cc.rapidev.qqbot.agent.handler.AgentMessageHandler;
import cc.rapidev.qqbot.agent.service.AgentService;
import cc.rapidev.qqbot.extension.Extension;

/**
 * @author leibrother
 */
public class AgentExtension implements Extension {

    private AgentService service;

    @Override
    public void ready(Bot bot) {
        this.service = new AgentService(bot);
        bot.add(service);
        AgentMessageHandler handler = new AgentMessageHandler(service);
        bot.dispatcher().register(handler);
    }

    @Override
    public void destroy() {
        service.close();
    }

}
