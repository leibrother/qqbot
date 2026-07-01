package cc.rapidev.qqbot.extension.settings.component;

import cc.rapidev.qqbot.common.Scope;
import cc.rapidev.qqbot.common.markdown.MarkdownUI;
import cc.rapidev.qqbot.common.markdown.component.BlockComponent;
import cc.rapidev.qqbot.common.markdown.component.Listview;
import cc.rapidev.qqbot.common.utils.StringUtils;
import cc.rapidev.qqbot.message.MessageContext;
import cc.rapidev.qqbot.message.model.MessageGeneric;

import java.util.ArrayList;
import java.util.List;

/**
 * @author leibrother
 */
public class Selection extends SettingItem {

    private final List<String> options;

    public Selection(String key, String name, String description, List<String> options, Scope scope) {
        super(key, name, description, scope);
        this.options = options;
    }

    public String getValue(MessageContext context) {
        String value = super.getValue(context);
        if (value != null && !options.contains(value)) {
            return null;
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

        private List<String> options = new ArrayList<>();

        public Builder options(List<String> options) {
            this.options = options;
            return this;
        }

        public Builder addOption(String option) {
            this.options.add(option);
            return this;
        }

        public Selection build() {
            return new Selection(key, name, description, options, scope);
        }

    }
}
