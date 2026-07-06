package cc.rapidev.qqbot.extension.settings.component;

import cc.rapidev.qqbot.common.Topic;
import cc.rapidev.qqbot.common.markdown.MarkdownUI;
import cc.rapidev.qqbot.common.markdown.component.BlockComponent;
import cc.rapidev.qqbot.common.markdown.component.Item;
import cc.rapidev.qqbot.common.markdown.component.Listview;
import cc.rapidev.qqbot.extension.command.Command;
import cc.rapidev.qqbot.extension.settings.persistence.SettingPersistenceService;
import cc.rapidev.qqbot.message.MessageContext;
import cc.rapidev.qqbot.message.model.MessageGeneric;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author leibrother
 */
public class Checkbox extends SettingItem {

    private final static String separator = ",";

    private final Set<String> options;
    private final Set<String> defaultOptions;

    public Checkbox(Builder builder) {
        super(builder);
        this.options = Stream.of(builder.options, builder.defaultOptions).flatMap(Set::stream).collect(Collectors.toUnmodifiableSet());
        this.defaultOptions = Collections.unmodifiableSet(builder.defaultOptions);
    }

    public String getValue(SettingPersistenceService persistence, Topic topic) {
        String value = super.getValue(persistence, topic);
        if (value == null) {
            return String.join(separator, defaultOptions);
        }
        List<String> list = Stream.of(value.split(separator)).filter(options::contains).toList();
        if (list.isEmpty()) {
            return String.join(separator, defaultOptions);
        }
        return String.join(separator, list);
    }

    public Set<String> getValues(MessageContext context) {
        SettingPersistenceService persistence = context.use(SettingPersistenceService.class);
        return getValues(persistence, context.topic());
    }

    public Set<String> getValues(SettingPersistenceService persistence, Topic topic) {
        String value = getValue(persistence, topic);
        return Set.of(value.split(separator));
    }

    public void setValue(MessageContext context, Set<String> values) {
        String value = values == null || values.isEmpty() ? null : String.join(separator, values);
        setValue(context, value);
    }

    public void setValue(SettingPersistenceService persistence, Topic topic, Set<String> values) {
        String value = values == null || values.isEmpty() ? null : String.join(separator, values);
        setValue(persistence, topic, value);
    }

    @Override
    public BlockComponent render(MessageContext context) {
        if (options.isEmpty()) {
            return MarkdownUI.block(MarkdownUI.text("暂无可选项"));
        }
        Set<String> values = getValues(context);
        Listview view = MarkdownUI.list();
        for (String option : this.options) {
            Item item = MarkdownUI.item();
            if (values.contains(option)) {
                item.add(MarkdownUI.cmdInput("取消 " + option, "取消", false));
            } else {
                item.add(MarkdownUI.cmdInput("选择 " + option, "选择", false));
            }
            item.add(MarkdownUI.whitespace(), MarkdownUI.bold(option));
            view.add(item);
        }
        return view;
    }

    @Override
    public boolean set(MessageContext context, MessageGeneric message) {
        Command command = new Command(message.content());
        Optional<Command> sel = command.match("选择");
        if (sel.isPresent()) {
            String option = sel.get().content().trim();
            Set<String> values = getValues(context);
            if (this.options.contains(option) && !values.contains(option)) {
                Set<String> newValues = new HashSet<>(values);
                newValues.add(option);
                setValue(context, newValues);
                return true;
            }
        }
        Optional<Command> rem = command.match("取消");
        if (rem.isPresent()) {
            String option = rem.get().content().trim();
            Set<String> values = getValues(context);
            if (this.options.contains(option) && values.contains(option)) {
                Set<String> newValues = new HashSet<>(values);
                newValues.remove(option);
                setValue(context, newValues);
                return true;
            }
        }
        return false;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends SettingItemBuilder<Builder> {

        private Set<String> options = new LinkedHashSet<>();
        private Set<String> defaultOptions = new LinkedHashSet<>();

        public Builder options(Set<String> options) {
            this.options = new LinkedHashSet<>(options);
            return this;
        }

        public Builder defaultOptions(Set<String> defaultOptions) {
            this.defaultOptions = new LinkedHashSet<>(defaultOptions);
            return this;
        }

        public Builder addOption(String option) {
            this.options.add(option);
            return this;
        }

        public Builder addDefaultOption(String defaultOption) {
            this.defaultOptions.add(defaultOption);
            return this;
        }

        public Checkbox build() {
            return new Checkbox(this);
        }
    }


}
