package cc.rapidev.qqbot.extension.settings.component;

import cc.rapidev.qqbot.common.Topic;
import cc.rapidev.qqbot.extension.settings.RenderContext;
import cc.rapidev.qqbot.extension.settings.SettingScope;
import cc.rapidev.qqbot.extension.settings.repository.SettingRepository;
import cc.rapidev.qqbot.message.MessageContext;
import cc.rapidev.qqbot.message.model.MessageGeneric;

import java.util.function.Function;

/**
 * @author leibrother
 */
public class Button extends SettingItem {

    private Function<RenderContext, String> onclick;

    public Button(String key, String name, String description, SettingScope scope) {
        super(key, name, description, scope);
    }

    public void onclick(Function<RenderContext, String> onclick) {
        this.onclick = onclick;
    }

    @Override
    protected void setValue(SettingRepository repository, Topic topic, String value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean set(MessageContext context, SettingRepository repository, MessageGeneric message) {
        return false;
    }

    @Override
    public String render(RenderContext context) {
        if (this.onclick != null) {
            String result = onclick.apply(context);
            if (result != null) {
                return result;
            }
        }
        return "";
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends SettingItemBuilder<Builder> {

        private Function<RenderContext, String> onclick;

        public Builder onclick(Function<RenderContext, String> onclick) {
            this.onclick = onclick;
            return this;
        }

        public Button build() {
            Button button = new Button(key, name, description, scope);
            button.onclick(onclick);
            return button;
        }

    }

}
