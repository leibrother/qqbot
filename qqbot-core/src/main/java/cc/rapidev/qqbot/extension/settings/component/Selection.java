package cc.rapidev.qqbot.extension.settings.component;

import cc.rapidev.qqbot.common.Topic;
import cc.rapidev.qqbot.common.markdown.MarkdownUI;
import cc.rapidev.qqbot.common.markdown.component.BlockComponent;
import cc.rapidev.qqbot.common.markdown.component.Listview;
import cc.rapidev.qqbot.common.utils.StringUtils;
import cc.rapidev.qqbot.extension.settings.persistence.SettingPersistenceService;
import cc.rapidev.qqbot.message.MessageContext;
import cc.rapidev.qqbot.message.model.MessageGeneric;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author leibrother
 */
public class Selection extends SettingItem {

    private final Set<String> options;
    private final String defaultOption;

    public Selection(Builder builder) {
        super(builder);
        HashSet<String> options = new HashSet<>(builder.options);
        options.add(builder.defaultOption);
        this.options = Collections.unmodifiableSet(options);
        this.defaultOption = builder.defaultOption;
    }

    public String getValue(SettingPersistenceService persistence, Topic topic) {
        String value = super.getValue(persistence, topic);
        if (value == null || !options.contains(value)) {
            return this.defaultOption;
        }
        return value;
    }

    @Override
    public BlockComponent render(MessageContext context) {
        if (options.isEmpty()) {
            return MarkdownUI.block(MarkdownUI.text("暂无可选项"));
        }
        String value = getValue(context);
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

    public static class Builder extends SettingItemBuilder<Builder> {

        protected Set<String> options = new HashSet<>();
        protected String defaultOption = null;

        public Builder options(Set<String> options) {
            this.options = options;
            return this;
        }

        public Builder addOption(String option) {
            this.options.add(option);
            return this;
        }

        public Builder setDefault(String value) {
            this.defaultOption = value;
            return this;
        }

        public Selection build() {
            return new Selection(this);
        }

    }
}
