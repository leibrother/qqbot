package cc.rapidev.qqbot.extension.settings.component;

import cc.rapidev.qqbot.common.Scope;
import cc.rapidev.qqbot.common.Topic;
import cc.rapidev.qqbot.common.utils.StringUtils;
import cc.rapidev.qqbot.extension.settings.RenderContext;
import cc.rapidev.qqbot.extension.settings.repository.SettingRepository;
import cc.rapidev.qqbot.message.MessageContext;
import cc.rapidev.qqbot.message.model.MessageGeneric;

/**
 * @author leibrother
 */
public class Input extends SettingItem {

    private boolean password;

    public Input(String key, String name, String description, Scope scope) {
        super(key, name, description, scope);
    }

    public void password(boolean password) {
        this.password = password;
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
    public String getViewValue(SettingRepository repository, Topic topic) {
        String value = getValue(repository, topic);
        if (value != null) {
            if (password) {
                value = "\\*\\*\\*\\*\\*\\*";
            }
        }
        return value;
    }

    @Override
    public String render(RenderContext context) {
        String value = getViewValue(context.repository(), context.topic());
        StringBuilder builder = new StringBuilder();
        if (value != null) {
            builder.append("**").append(value).append("**");
            builder.append("\n");
            builder.append("> 发送消息替换当前值");
        } else {
            builder.append("> 发送消息设置值");
        }
        return builder.toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends SettingBuilder<Builder> {

        private boolean password = false;

        public Builder password() {
            this.password = true;
            return this;
        }

        public Input build() {
            Input input = new Input(key, name, description, scope);
            input.password(password);
            return input;
        }

    }

}
