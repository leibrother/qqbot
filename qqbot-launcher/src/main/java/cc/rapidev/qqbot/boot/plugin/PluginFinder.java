package cc.rapidev.qqbot.boot.plugin;

import java.util.List;
import java.util.Optional;

/**
 * @author leibrother
 */
public interface PluginFinder {

    List<Plugin> plugins();

    Optional<Plugin> find(String id);

}
