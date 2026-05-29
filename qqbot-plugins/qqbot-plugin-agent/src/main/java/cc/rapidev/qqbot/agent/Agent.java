package cc.rapidev.qqbot.agent;

import cc.rapidev.qqbot.Bot;
import cc.rapidev.qqbot.agent.setting.AgentSettingService;
import cc.rapidev.qqbot.extension.Extension;

/**
 * @author leibrother
 */
public class Agent implements Extension {

    private AgentSettingService settingService;

    @Override
    public void ready(Bot bot) {
        this.settingService = new AgentSettingService(bot);
    }

    @Override
    public void destroy() {
        settingService.close();
    }

}
