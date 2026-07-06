package cc.rapidev.qqbot.extension.settings.component;

import cc.rapidev.qqbot.common.Scope;
import cc.rapidev.qqbot.common.Topic;
import cc.rapidev.qqbot.common.utils.CastUtils;
import cc.rapidev.qqbot.extension.settings.Setting;
import cc.rapidev.qqbot.extension.settings.persistence.SettingPersistenceService;
import cc.rapidev.qqbot.message.MessageContext;
import cc.rapidev.qqbot.message.model.MessageGeneric;

/**
 * @author leibrother
 */
public abstract class SettingItem extends Setting {

    private final Scope scope;

    public SettingItem(SettingItemBuilder<?> builder) {
        super(builder);
        this.scope = builder.scope == null ? Scope.GLOBAL : builder.scope;
    }

    public SettingItem(String key, String name, String description, Scope scope) {
        super(key, name, description);
        this.scope = scope == null ? Scope.GLOBAL : scope;
    }

    final public Scope scope() {
        return this.scope;
    }

    final public String scopeKey(Topic topic) {
        if (this.scope == Scope.GLOBAL) {
            return "GLOBAL";
        }
        return "TOPIC:" + topic.code();
    }

    /**
     * 获取值
     *
     * @param persistence 持久化服务
     * @param topic       topic
     * @return value
     */
    public String getValue(SettingPersistenceService persistence, Topic topic) {
        return persistence.getValue(this, scopeKey(topic));
    }

    /**
     * 设置值
     *
     * @param persistence 持久化服务
     * @param topic       topic
     * @param value       value
     */
    public void setValue(SettingPersistenceService persistence, Topic topic, String value) {
        persistence.setValue(this, scopeKey(topic), value);
    }

    /**
     * 获取值
     *
     * @param context 消息上下文
     * @return value
     */
    final public String getValue(MessageContext context) {
        SettingPersistenceService persistence = context.use(SettingPersistenceService.class);
        return getValue(persistence, context.topic());
    }

    /**
     * 设置值
     *
     * @param context 消息上下文
     * @param value   value
     */
    final public void setValue(MessageContext context, String value) {
        SettingPersistenceService persistence = context.use(SettingPersistenceService.class);
        setValue(persistence, context.topic(), value);
    }

    /**
     * 获取视图显示的值
     *
     * @param context 消息上下文
     * @return 在Markdown显示的值
     */
    public String getShowValue(MessageContext context) {
        return this.getValue(context);
    }

    /**
     * 用户设置值
     *
     * @param context 消息上下文
     * @param message message
     * @return 是否成功
     */
    public abstract boolean set(MessageContext context, MessageGeneric message);

    public static class SettingItemBuilder<B extends SettingItemBuilder<B>> extends SettingBuilder<B> {

        private Scope scope = Scope.TOPIC;

        public B global() {
            this.scope = Scope.GLOBAL;
            return CastUtils.cast(this);
        }

    }

}
