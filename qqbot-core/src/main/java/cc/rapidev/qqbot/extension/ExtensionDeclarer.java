package cc.rapidev.qqbot.extension;

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

}
