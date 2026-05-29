package cc.rapidev.qqbot.extension.settings.component;

import cc.rapidev.qqbot.common.Scope;
import cc.rapidev.qqbot.common.Topic;
import cc.rapidev.qqbot.extension.settings.Setting;
import cc.rapidev.qqbot.extension.settings.repository.SettingRepository;
import cc.rapidev.qqbot.message.MessageContext;
import cc.rapidev.qqbot.message.model.MessageGeneric;

/**
 * @author leibrother
 */
public abstract class SettingItem extends Setting {

    private final Scope scope;

    public SettingItem(String key, String name, String description, Scope scope) {
        super(key, name, description);
        this.scope = scope == null ? Scope.GLOBAL : scope;
    }

    public Scope scope() {
        return this.scope;
    }

    private String scopeKey(Topic topic) {
        return this.scope == Scope.GLOBAL ? "GLOBAL" : topic.code();
    }

    /**
     * 获取值
     *
     * @param context 消息上下文
     * @return 值
     */
    public String getValue(MessageContext context) {
        SettingRepository repository = context.use(SettingRepository.class);
        Topic topic = context.topic();
        return getValue(repository, topic);
    }

    /**
     * 获取值
     *
     * @param repository repository
     * @param topic      topic
     * @return value
     */
    public String getValue(SettingRepository repository, Topic topic) {
        return repository.get(completedKey(), scopeKey(topic));
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
     * 用户设置值
     *
     * @param context    消息上下文
     * @param repository repository
     * @param message    message
     * @return 是否成功
     */
    public abstract boolean set(MessageContext context, SettingRepository repository, MessageGeneric message);

}
