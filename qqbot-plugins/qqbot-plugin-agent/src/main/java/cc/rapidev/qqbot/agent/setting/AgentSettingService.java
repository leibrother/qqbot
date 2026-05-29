package cc.rapidev.qqbot.agent.setting;

import cc.rapidev.qqbot.Bot;
import cc.rapidev.qqbot.extension.settings.SettingGroup;
import cc.rapidev.qqbot.extension.settings.SettingService;

/**
 * @author leibrother
 */
public class AgentSettingService implements AutoCloseable {

    private final SettingGroup root;
    private final Runnable remover;
    private final ModelSetting modelSetting;

    public AgentSettingService(Bot bot) {
        this.root = new SettingGroup("agent", "智能体", "配置模型、技能、MCP等");
        this.remover = bot.use(SettingService.class).append(this.root);
        this.modelSetting = new ModelSetting(this.root);
    }

    @Override
    public void close() {
        this.remover.run();
    }

}
