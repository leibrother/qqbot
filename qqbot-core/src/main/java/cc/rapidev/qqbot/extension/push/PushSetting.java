package cc.rapidev.qqbot.extension.push;

import cc.rapidev.qqbot.common.Topic;
import cc.rapidev.qqbot.extension.settings.component.Selection;
import cc.rapidev.qqbot.extension.settings.persistence.SettingPersistenceService;

import java.util.Set;

/**
 * @author leibrother
 */
public class PushSetting extends Selection {

    private final static String OPEN = "开启";
    private final static String CLOSE = "关闭";

    private final PushService service;

    public PushSetting(PushService service) {
        Builder builder = Selection.builder()
                .key("push")
                .name("主动推送")
                .description("是否开启主动推送")
                .options(Set.of(OPEN, CLOSE))
                .setDefault(CLOSE);
        super(builder);
        this.service = service;
    }

    @Override
    public void setValue(SettingPersistenceService ignore, Topic topic, String value) {
        if (OPEN.equals(value)) {
            this.service.open(topic);
        } else if (CLOSE.equals(value)) {
            this.service.close(topic);
        }
    }

    @Override
    public String getValue(SettingPersistenceService ignore, Topic topic) {
        if (service.isOpen(topic)) {
            return OPEN;
        }
        return CLOSE;
    }

}
