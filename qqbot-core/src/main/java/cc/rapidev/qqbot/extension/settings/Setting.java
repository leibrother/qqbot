package cc.rapidev.qqbot.extension.settings;

import cc.rapidev.qqbot.common.markdown.component.BlockComponent;
import cc.rapidev.qqbot.common.utils.CastUtils;
import cc.rapidev.qqbot.message.MessageContext;

/**
 * @author leibrother
 */
public abstract class Setting {

    private final String key;
    private final String name;
    private final String description;
    private Setting parent;

    public Setting(SettingBuilder<?> builder) {
        this(builder.key, builder.name, builder.description);
    }

    public Setting(String key, String name, String description) {
        this.key = key;
        this.name = name;
        this.description = description;
    }

    public String key() {
        return this.key;
    }

    public String name() {
        return this.name;
    }

    public String description() {
        return this.description;
    }

    public Setting parent() {
        return parent;
    }

    public void parent(Setting parent) {
        this.parent = parent;
    }

    public String path() {
        Setting parent = parent();
        if (parent == null) {
            return this.name();
        } else {
            return parent.path() + " / " + this.name();
        }
    }

    public String completedKey() {
        Setting parent = parent();
        if (parent == null) {
            return this.key();
        } else {
            return parent.completedKey() + "." + this.key();
        }
    }

    public abstract BlockComponent render(MessageContext context);

    /**
     * 通用构建器
     */
    public abstract static class SettingBuilder<B extends SettingBuilder<B>> {

        protected String key;
        protected String name;
        protected String description;

        public B key(String key) {
            this.key = key;
            return CastUtils.cast(this);
        }

        public B name(String name) {
            this.name = name;
            return CastUtils.cast(this);
        }

        public B description(String description) {
            this.description = description;
            return CastUtils.cast(this);
        }

    }

}
