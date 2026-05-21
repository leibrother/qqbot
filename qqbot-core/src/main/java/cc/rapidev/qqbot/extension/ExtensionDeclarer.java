package cc.rapidev.qqbot.extension;

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
    }

}
