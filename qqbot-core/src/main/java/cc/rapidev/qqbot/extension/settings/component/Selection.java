package cc.rapidev.qqbot.extension.settings.component;

import cc.rapidev.qqbot.common.Topic;
import cc.rapidev.qqbot.common.markdown.MarkdownUI;
import cc.rapidev.qqbot.common.markdown.component.BlockComponent;
import cc.rapidev.qqbot.common.markdown.component.Listview;
import cc.rapidev.qqbot.common.utils.StringUtils;
import cc.rapidev.qqbot.extension.settings.persistence.SettingPersistenceService;
import cc.rapidev.qqbot.message.MessageContext;
import cc.rapidev.qqbot.message.model.MessageGeneric;

import java.util.ArrayList;
import java.util.List;

/**
 * @author leibrother
 */
public class Selection extends SettingItem {

    private final List<String> options;
    private final String defaultValue;

    public Selection(Builder builder) {
        super(builder.key(), builder.name(), builder.description(), builder.scope());
        this.options = builder.options;
        this.defaultValue = builder.defaultValue;
    }

    public String getValue(SettingPersistenceService persistence, Topic topic) {
        String value = super.getValue(persistence, topic);
        if (value == null || !options.contains(value)) {
            return this.defaultValue;
        }
        return value;
    }

    @Override
    public BlockComponent render(MessageContext context) {
        String value = getValue(context);
        if (options.isEmpty()) {
            return MarkdownUI.block(MarkdownUI.text("暂无可选项"));
        }
        Listview list = MarkdownUI.list();
        for (String option : options) {
            if (option.equals(value)) {
                list.add(MarkdownUI.item(MarkdownUI.text(option), MarkdownUI.whitespace(), MarkdownUI.text("√")));
            } else {
                list.add(MarkdownUI.item(MarkdownUI.cmd(context.topic(), option)));
            }
        }
        return list;
    }

    @Override
    public boolean set(MessageContext context, MessageGeneric message) {
        String content = message.content().trim();
        if (!StringUtils.isEmpty(content) && options.contains(content)) {
            setValue(context, content);
            return true;
        }
        return false;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends SettingBuilder<Builder> {

        protected List<String> options = new ArrayList<>();
        protected String defaultValue = null;

        public Builder options(List<String> options) {
            this.options = options;
            return this;
        }

        public Builder addOption(String option) {
            this.options.add(option);
            return this;
        }

        public Builder setDefault(String value) {
            if (this.defaultValue != null) {
                this.options.remove(this.defaultValue);
            }
            this.defaultValue = value;
            if (!this.options.contains(value)) {
                this.options.add(value);
            }
            return this;
        }

        public Selection build() {
            return new Selection(this);
        }

    }
}
