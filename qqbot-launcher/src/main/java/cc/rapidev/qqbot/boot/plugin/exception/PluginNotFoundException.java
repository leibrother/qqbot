package cc.rapidev.qqbot.boot.plugin.exception;

/**
 * @author leibrother
 */
public class PluginNotFoundException extends PluginException {

    private final String id;

    public PluginNotFoundException(String id) {
        super("not found find plugin: " + id);
        this.id = id;
    }

    public String id() {
        return this.id;
    }

}
