package cc.rapidev.qqbot.agent.setting;

import cc.rapidev.qqbot.agent.ApiFormat;
import cc.rapidev.qqbot.extension.settings.SettingGroup;
import cc.rapidev.qqbot.extension.settings.component.Button;
import cc.rapidev.qqbot.extension.settings.component.Input;
import cc.rapidev.qqbot.extension.settings.component.Selection;

import java.util.Arrays;

/**
 * @author leibrother
 */
public class ModelSetting {

    private final SettingGroup parent;

    public ModelSetting(SettingGroup parent) {
        this.parent = parent;
        init();
    }

    private void init() {
        SettingGroup group = new SettingGroup("model", "模型", "设置聊天时使用的模型");
        this.parent.append(group);
        // 服务商
        Selection format = Selection.builder()
                .key("format")
                .name("API 格式")
                .description("选择API格式")
                .options(Arrays.stream(ApiFormat.values()).map(ApiFormat::name).toList())
                .build();
        group.append(format);
        // 请求地址
        Input baseurl = Input.builder()
                .key("baseurl")
                .name("请求地址")
                .description("输入请求地址")
                .build();
        group.append(baseurl);
        // API密钥
        Input apikey = Input.builder()
                .key("apikey")
                .name("API 密钥")
                .description("输入API密钥")
                .build();
        group.append(apikey);
        // 模型名称
        Input model = Input.builder()
                .key("name")
                .name("模型名称")
                .description("输入模型名称")
                .build();
        group.append(model);
        // 测试连接
        Button testbutton = Button.builder()
                .key("test")
                .name("测试连接")
                .onclick(_ -> "测试连接成功！")
                .build();
        group.append(testbutton);
    }

}
