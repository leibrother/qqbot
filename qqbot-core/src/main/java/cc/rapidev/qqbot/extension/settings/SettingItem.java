package cc.rapidev.qqbot.extension.settings;

import cc.rapidev.qqbot.common.Topic;
import cc.rapidev.qqbot.extension.settings.repository.SettingRepository;
import cc.rapidev.qqbot.message.MessageContext;
import cc.rapidev.qqbot.message.model.MessageGeneric;

/**
 * @author leibrother
 */
public abstract class SettingItem implements Setting {

    private final String key;
    private final String name;
    private final String description;
    private final SettingScope scope;
    private Setting parent;

    public SettingItem(String key, String name, String description, SettingScope scope) {
        this.key = key;
        this.name = name;
        this.description = description;
        this.scope = scope == null ? SettingScope.GLOBAL : scope;
    }

    @Override
    public String key() {
        return this.key;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public String description() {
        return this.description;
    }

    @Override
    public Setting parent() {
        return parent;
    }

    @Override
    public void parent(Setting parent) {
        this.parent = parent;
    }

    public SettingScope scope() {
        return this.scope;
    }

    private String scopeKey(Topic topic) {
        return this.scope == SettingScope.GLOBAL ? "GLOBAL" : topic.code();
    }

    protected String getValue(SettingRepository repository, Topic topic) {
        return repository.get(completedKey(), scopeKey(topic));
    }

    protected void setValue(SettingRepository repository, Topic topic, String value) {
        repository.set(completedKey(), scopeKey(topic), value);
    }

    public final String get(SettingRepository repository, Topic topic) {
        return this.getValue(repository, topic);
    }

    public abstract boolean set(MessageContext context, SettingRepository repository, MessageGeneric message);

    public static class SettingItemBuilder {

        protected String key;
        protected String name;
        protected String description;
        protected SettingScope scope;

        public SettingItemBuilder key(String key) {
            this.key = key;
            return this;
        }

        public SettingItemBuilder name(String name) {
            this.name = name;
            return this;
        }

        public SettingItemBuilder description(String description) {
            this.description = description;
            return this;
        }

        public SettingItemBuilder scope(SettingScope scope) {
            this.scope = scope;
            return this;
        }

    }

}
