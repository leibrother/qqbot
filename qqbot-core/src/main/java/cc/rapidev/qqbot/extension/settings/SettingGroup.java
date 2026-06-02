package cc.rapidev.qqbot.extension.settings;

import cc.rapidev.qqbot.common.Scope;
import cc.rapidev.qqbot.common.Topic;
import cc.rapidev.qqbot.common.utils.StringUtils;
import cc.rapidev.qqbot.extension.settings.component.SettingItem;
import cc.rapidev.qqbot.extension.settings.repository.SettingRepository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author leibrother
 */
public class SettingGroup extends Setting {

    private final List<Setting> children = new ArrayList<>();

    public SettingGroup(String key, String name, String description) {
        super(key, name, description);
    }

    public void append(Setting setting) {
        List<Setting> exists = this.children.stream().filter(child -> child.key().equals(setting.key())).toList();
        if (!exists.isEmpty()) {
            throw new IllegalArgumentException("设置项: %s 已存在，请勿重复添加！".formatted(setting.key()));
        }

        if (setting.parent() != null) {
            if (setting.parent() instanceof SettingGroup group) {
                group.remove(setting);
            }
        }
        setting.parent(this);
        this.children.add(setting);
    }

    public void remove(Setting setting) {
        this.children.removeIf(child -> child.key().equals(setting.key()));
    }

    @Override
    public String render(RenderContext context) {
        SettingRepository repository = context.repository();
        Topic topic = context.topic();
        boolean admin = context.admin();
        List<Setting> items;
        if (admin) {
            items = this.adminVisible();
        } else {
            items = this.topicVisible();
        }

        if (items.isEmpty()) {
            return "无设置项";
        }

        StringBuilder builder = new StringBuilder();
        Iterator<Setting> iterator = items.iterator();
        while (iterator.hasNext()) {
            Setting item = iterator.next();
            builder.append("**<qqbot-cmd-enter text=\"").append(item.name()).append("\"/>**");
            String description = item.description();
            if (item instanceof SettingItem settingItem) {
                String value = settingItem.getViewValue(repository, topic);
                if (value != null) {
                    description = value;
                }
            }
            if (StringUtils.isNotEmpty(description)) {
                builder.append("\n");
                builder.append("> ").append(description);
            }
            if (iterator.hasNext()) {
                builder.append("\n");
                builder.append("\n");
            }
        }

        return builder.toString();
    }

    /**
     * 管理员可见的设置项
     *
     * @return 设置项列表
     */
    private List<Setting> adminVisible() {
        List<Setting> items = new ArrayList<>();
        for (Setting setting : this.children) {
            if (setting instanceof SettingGroup group) {
                if (!group.adminVisible().isEmpty()) {
                    items.add(group);
                }
            } else {
                items.add(setting);
            }
        }
        return items;
    }

    /**
     * 主题可见的设置项
     *
     * @return 设置项列表
     */
    private List<Setting> topicVisible() {
        List<Setting> items = new ArrayList<>();
        for (Setting setting : this.children) {
            if (setting instanceof SettingGroup group) {
                if (!group.topicVisible().isEmpty()) {
                    items.add(setting);
                }
            } else if (setting instanceof SettingItem item) {
                if (item.scope() == Scope.TOPIC) {
                    items.add(setting);
                }
            }
        }
        return items;
    }

    public Setting findChild(String name) {
        for (Setting setting : this.children) {
            if (setting.name().equalsIgnoreCase(name)) {
                return setting;
            }
        }
        return null;
    }

}
