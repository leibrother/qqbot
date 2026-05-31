package cc.rapidev.qqbot.agent.service;

import cc.rapidev.qqbot.Bot;
import cc.rapidev.qqbot.agent.model.ModelSetting;
import cc.rapidev.qqbot.agent.model.ModelValues;
import cc.rapidev.qqbot.extension.settings.SettingGroup;
import cc.rapidev.qqbot.extension.settings.SettingService;
import cc.rapidev.qqbot.message.MessageContext;
import org.jspecify.annotations.Nullable;

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

    public @Nullable ModelValues getModelValues(MessageContext context) {
        return modelSetting.getValues(context);
    }

}
