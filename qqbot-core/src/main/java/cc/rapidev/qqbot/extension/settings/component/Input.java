package cc.rapidev.qqbot.extension.settings.component;

import cc.rapidev.qqbot.common.Scope;
import cc.rapidev.qqbot.common.markdown.MarkdownUI;
import cc.rapidev.qqbot.common.markdown.component.Block;
import cc.rapidev.qqbot.common.markdown.component.BlockComponent;
import cc.rapidev.qqbot.common.markdown.component.Component;
import cc.rapidev.qqbot.common.utils.StringUtils;
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
    public boolean set(MessageContext context, MessageGeneric message) {
        String content = message.content().trim();
        if (!StringUtils.isEmpty(content)) {
            setValue(context, content.trim());
            return true;
        }
        return false;
    }

    @Override
    public String getShowValue(MessageContext context) {
        String value = getValue(context);
        if (value != null) {
            if (password) {
                value = "\\*\\*\\*\\*\\*\\*";
            }
        }
        return value;
    }

    @Override
    public BlockComponent render(MessageContext context) {
        String value = getShowValue(context);
        Block<Component> block = MarkdownUI.block();
        if (value != null) {
            block.add(MarkdownUI.block(MarkdownUI.bold(value)));
            block.add(MarkdownUI.block(MarkdownUI.blockQuote("发送消息替换当前值")));
        } else {
            block.add(MarkdownUI.block(MarkdownUI.blockQuote("发送消息设置值")));
        }
        return block;
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
