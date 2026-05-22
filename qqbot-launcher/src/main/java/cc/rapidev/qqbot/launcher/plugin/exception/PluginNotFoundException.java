package cc.rapidev.qqbot.launcher.plugin.exception;

/**
 * @author leibrother
 */
public class PluginNotFoundException extends PluginException {

    private final String id;

    public PluginNotFoundException(String id) {
        super("plugin not found: " + id);
        this.id = id;
    }

    public String id() {
        return this.id;
    }

}
