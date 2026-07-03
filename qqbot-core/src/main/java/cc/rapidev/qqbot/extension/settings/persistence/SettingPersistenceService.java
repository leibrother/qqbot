package cc.rapidev.qqbot.extension.settings.persistence;

import cc.rapidev.qqbot.extension.settings.component.SettingItem;

/**
 * @author leibrother
 */
public interface SettingPersistenceService {

    String getValue(SettingItem setting, String scope);

    void setValue(SettingItem setting, String scope, String value);

}
