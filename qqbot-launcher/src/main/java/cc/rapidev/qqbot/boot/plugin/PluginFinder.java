package cc.rapidev.qqbot.boot.plugin;

import java.util.Optional;

/**
 * @author leibrother
 */
@FunctionalInterface
public interface PluginFinder {

    Optional<Plugin> find(String id);

}
