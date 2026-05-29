package cc.rapidev.qqbot.extension.settings;

import cc.rapidev.qqbot.common.Topic;
import cc.rapidev.qqbot.extension.settings.repository.SettingRepository;
import cc.rapidev.qqbot.message.model.Author;

/**
 * @author leibrother
 */
public record RenderContext(
        SettingRepository repository,
        Topic topic,
        Author author,
        boolean admin
) {
}
