package cc.rapidev.qqbot.extension.settings.item;

import cc.rapidev.qqbot.common.Topic;
import cc.rapidev.qqbot.common.utils.StringUtils;
import cc.rapidev.qqbot.extension.settings.SettingItem;
import cc.rapidev.qqbot.extension.settings.SettingScope;
import cc.rapidev.qqbot.extension.settings.repository.SettingRepository;
import cc.rapidev.qqbot.message.MessageContext;
import cc.rapidev.qqbot.message.model.MessageGeneric;

/**
 * @author leibrother
 */
public class Input extends SettingItem {

    public Input(String key, String name, String description, SettingScope scope) {
        super(key, name, description, scope);
    }

    @Override
    public boolean set(MessageContext context, SettingRepository repository, MessageGeneric message) {
        String content = message.content().trim();
        if (!StringUtils.isEmpty(content)) {
            setValue(repository, context.topic(), content.trim());
            return true;
        }
        return false;
    }

    @Override
    public String render(SettingRepository repository, Topic topic, boolean admin) {
        String value = getValue(repository, topic);
        StringBuilder builder = new StringBuilder();
        if (value != null) {
            builder.append("当前值：");
            builder.append("**").append(value).append("**");
            builder.append("\n");
        }
        builder.append("> 请发送要设置的值");
        return builder.toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends SettingItemBuilder {

        public Input build() {
            return new Input(key, name, description, scope);
        }

    }

}
