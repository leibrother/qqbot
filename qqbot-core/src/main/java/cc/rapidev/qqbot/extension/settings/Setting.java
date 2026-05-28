package cc.rapidev.qqbot.extension.settings;

import cc.rapidev.qqbot.common.Topic;
import cc.rapidev.qqbot.extension.settings.repository.SettingRepository;

/**
 * @author leibrother
 */
public interface Setting {

    String key();

    String name();

    String description();

    String render(SettingRepository repository, Topic topic, boolean admin);

    Setting parent();

    void parent(Setting setting);

    default String path() {
        Setting parent = parent();
        if (parent == null) {
            return this.name();
        }else{
            return parent.path() + " / " + this.name();
        }
    }

    default String completedKey() {
        Setting parent = parent();
        if (parent == null) {
            return this.key();
        } else {
            return parent.completedKey() + "." + this.key();
        }
    }

}
