package cc.rapidev.qqbot.extension.settings.item;

import cc.rapidev.qqbot.common.Topic;
import cc.rapidev.qqbot.extension.settings.SettingItem;
import cc.rapidev.qqbot.extension.settings.SettingScope;
import cc.rapidev.qqbot.extension.settings.repository.SettingRepository;
import cc.rapidev.qqbot.message.MessageContext;
import cc.rapidev.qqbot.message.model.MessageGeneric;

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


    @Override
    public String render(SettingRepository repository, Topic topic, boolean admin) {
        return "请发送要设置的值";
    }

    @Override
    public boolean set(MessageContext context, SettingRepository repository, MessageGeneric message) {
        return false;
    }
}
