package cc.rapidev.qqbot.agent.model;

import cc.rapidev.qqbot.api.model.Message;
import cc.rapidev.qqbot.common.Topic;
import cc.rapidev.qqbot.common.utils.StringUtils;
import cc.rapidev.qqbot.extension.settings.RenderContext;
import cc.rapidev.qqbot.extension.settings.SettingGroup;
import cc.rapidev.qqbot.extension.settings.component.Button;
import cc.rapidev.qqbot.extension.settings.component.Input;
import cc.rapidev.qqbot.extension.settings.component.Selection;
import cc.rapidev.qqbot.extension.settings.repository.SettingRepository;
import cc.rapidev.qqbot.message.MessageContext;
import io.agentscope.core.message.Msg;
import io.agentscope.core.model.GenerateOptions;
import io.agentscope.core.model.Model;
import org.jspecify.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

/**
 * @author leibrother
 */
public class ModelSetting {

    private final Selection formatSetting;
    private final Input baseurlSetting;
    private final Input apikeySetting;
    private final Input modelnameSetting;

    public ModelSetting(SettingGroup parent) {
        SettingGroup group = new SettingGroup("model", "模型", "设置聊天时使用的模型");
        parent.append(group);
        // 服务商
        this.formatSetting = Selection.builder()
                .key("format")
                .name("API 格式")
                .description("选择API格式")
                .options(Arrays.stream(ApiFormat.values()).map(ApiFormat::name).toList())
                .build();
        group.append(formatSetting);
        // 请求地址
        this.baseurlSetting = Input.builder()
                .key("baseurl")
                .name("请求地址")
                .description("输入请求地址")
                .build();
        group.append(baseurlSetting);
        // API密钥
        this.apikeySetting = Input.builder()
                .key("apikey")
                .name("API 密钥")
                .description("输入API密钥")
                .password()
                .build();
        group.append(apikeySetting);
        // 模型名称
        this.modelnameSetting = Input.builder()
                .key("modelname")
                .name("模型名称")
                .description("输入模型名称")
                .build();
        group.append(modelnameSetting);
        // 测试连接
        Button testbutton = Button.builder()
                .key("test")
                .name("测试连接")
                .onclick(this::test)
                .build();
        group.append(testbutton);
    }

    private String test(RenderContext context) {
        ModelValues values;
        try {
            values = getValues(context.repository(), context.topic());
        } catch (IllegalStateException e) {
            return e.getMessage();
        }
        Model model = ModelFactory.produce(values);
        Msg msg = Msg.builder().textContent("hello").build();
        model.stream(List.of(msg), List.of(), GenerateOptions.builder().build()).blockLast();
        return "测试连接成功";
    }

    public ModelValues getValues(SettingRepository repository, Topic topic) {
        String format = formatSetting.getValue(repository, topic);
        String baseurl = baseurlSetting.getValue(repository, topic);
        String apikey = apikeySetting.getValue(repository, topic);
        String modelname = modelnameSetting.getValue(repository, topic);
        if (StringUtils.isEmpty(format)) {
            throw new IllegalStateException("请设置模型API格式");
        }
        if (StringUtils.isEmpty(baseurl)) {
            throw new IllegalStateException("请设置模型请求地址");
        }
        if (StringUtils.isEmpty(apikey)) {
            throw new IllegalStateException("请设置模型API密钥");
        }
        if (StringUtils.isEmpty(modelname)) {
            throw new IllegalStateException("请设置模型名称");
        }
        return new ModelValues(ApiFormat.valueOf(format), baseurl, apikey, modelname);
    }

    public @Nullable ModelValues getValues(MessageContext context) {
        SettingRepository repository = context.use(SettingRepository.class);
        try {
            return getValues(repository, context.topic());
        } catch (IllegalStateException e) {
            context.reply(Message.text(e.getMessage()));
            return null;
        }
    }

}
