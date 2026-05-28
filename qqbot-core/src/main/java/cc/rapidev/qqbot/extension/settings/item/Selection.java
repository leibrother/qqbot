package cc.rapidev.qqbot.extension.settings.item;

import cc.rapidev.qqbot.common.Topic;
import cc.rapidev.qqbot.common.utils.StringUtils;
import cc.rapidev.qqbot.extension.settings.SettingItem;
import cc.rapidev.qqbot.extension.settings.SettingScope;
import cc.rapidev.qqbot.extension.settings.repository.SettingRepository;
import cc.rapidev.qqbot.message.MessageContext;
import cc.rapidev.qqbot.message.model.MessageGeneric;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author leibrother
 */
public class Selection extends SettingItem {

    private final List<String> options;

    public Selection(String key, String name, String description, List<String> options, SettingScope scope) {
        super(key, name, description, scope);
        this.options = options;
    }

    protected String getValue(SettingRepository repository, Topic topic) {
        String value = super.getValue(repository, topic);
        if (value != null && !options.contains(value)) {
            return null;
        }
        return value;
    }

    @Override
    public String render(SettingRepository repository, Topic topic, boolean admin) {
        String value = getValue(repository, topic);
        StringBuilder builder = new StringBuilder();
        if (options.isEmpty()) {
            builder.append("暂无可选项");
        } else {
            Iterator<String> iterator = options.iterator();
            while (iterator.hasNext()) {
                String option = iterator.next();
                if (option.equals(value)) {
                    builder.append("- ").append(option).append(" √");
                } else {
                    builder.append("- ").append("<qqbot-cmd-enter text=\"").append(option).append("\"/>");
                }
                if (iterator.hasNext()) {
                    builder.append("\n");
                }
            }
        }
//        if (value != null) {
//            builder.append("当前选择：").append("**").append(value).append("**");
//            builder.append("\n");
//        }
//        if (options.isEmpty()) {
//            builder.append("暂无可选项");
//        } else {
//            builder.append("可选：");
//            for (String option : options) {
//                builder.append("\n");
//                builder.append("- ").append("<qqbot-cmd-enter text=\"").append(option).append("\"/>");
//            }
//        }
        return builder.toString();
    }

    @Override
    public boolean set(MessageContext context, SettingRepository repository, MessageGeneric message) {
        String content = message.content().trim();
        if (!StringUtils.isEmpty(content) && options.contains(content)) {
            setValue(repository, context.topic(), content);
            return true;
        }
        return false;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends SettingItemBuilder {

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
