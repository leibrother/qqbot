package cc.rapidev.qqbot.extension;

import cc.rapidev.qqbot.extension.admin.AdminExtension;
import cc.rapidev.qqbot.extension.command.CommandExtension;
import cc.rapidev.qqbot.extension.push.PushExtension;
import cc.rapidev.qqbot.extension.settings.SettingsExtension;
import cc.rapidev.qqbot.extension.template.TemplateExtension;

import java.util.ArrayList;
import java.util.List;

/**
 * @author leibrother
 */
public class ExtensionDeclarer {

    private static final List<Class<? extends Extension>> declared = new ArrayList<>();

    public static List<Class<? extends Extension>> declared() {
        return declared;
    }

    public static void declare(Class<? extends Extension> extension) {
        if (declared.contains(extension)) {
            return;
        }
        declared.add(extension);
    }

    public static void undeclare(Class<? extends Extension> extension) {
        if (!declared.contains(extension)) {
            return;
        }
        declared.remove(extension);
    }

    // 一些内置扩展
    static {
        // 模板渲染器扩展
        declare(TemplateExtension.class);
        // 指令消息处理扩展
        declare(CommandExtension.class);
        // 管理员扩展
        declare(AdminExtension.class);
        // 设置中心扩展
        declare(SettingsExtension.class);
        // 推送扩展
        declare(PushExtension.class);
    }

}
