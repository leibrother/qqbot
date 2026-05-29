package cc.rapidev.qqbot.extension.settings.component;

import cc.rapidev.qqbot.common.Topic;
import cc.rapidev.qqbot.common.utils.CastUtils;
import cc.rapidev.qqbot.extension.settings.Setting;
import cc.rapidev.qqbot.extension.settings.SettingScope;
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

    /**
     * 获取值
     *
     * @param repository repository
     * @param topic      topic
     * @return value
     */
    protected String getValue(SettingRepository repository, Topic topic) {
        return repository.get(completedKey(), scopeKey(topic));
    }

    /**
     * 将值持久化
     *
     * @param repository repository
     * @param topic      topic
     * @param value      value
     */
    protected void setValue(SettingRepository repository, Topic topic, String value) {
        repository.set(completedKey(), scopeKey(topic), value);
    }

    /**
     * 获取视图显示的值
     *
     * @param repository repository
     * @param topic      topic
     * @return 在Markdown显示的值
     */
    public String getViewValue(SettingRepository repository, Topic topic) {
        return this.getValue(repository, topic);
    }

    /**
     * 用户设置值
     *
     * @param context    消息上下文
     * @param repository repository
     * @param message    message
     * @return 是否成功
     */
    public abstract boolean set(MessageContext context, SettingRepository repository, MessageGeneric message);

    /**
     * 通用构建器
     */
    protected abstract static class SettingItemBuilder<B extends SettingItemBuilder<B>> {

        protected String key;
        protected String name;
        protected String description;
        protected SettingScope scope = SettingScope.TOPIC;

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

        public B global() {
            this.scope = SettingScope.GLOBAL;
            return CastUtils.cast(this);
        }

    }

}
