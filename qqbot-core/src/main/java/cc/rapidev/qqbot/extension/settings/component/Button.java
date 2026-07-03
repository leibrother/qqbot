package cc.rapidev.qqbot.extension.settings.component;

import cc.rapidev.qqbot.common.Scope;
import cc.rapidev.qqbot.common.Topic;
import cc.rapidev.qqbot.common.markdown.MarkdownUI;
import cc.rapidev.qqbot.common.markdown.component.BlockComponent;
import cc.rapidev.qqbot.extension.settings.persistence.SettingPersistenceService;
import cc.rapidev.qqbot.message.MessageContext;
import cc.rapidev.qqbot.message.model.MessageGeneric;

import java.util.function.Function;

/**
 * @author leibrother
 */
public class Button extends SettingItem {

    private Function<MessageContext, String> onclick;

    public Button(String key, String name, String description, Scope scope) {
        super(key, name, description, scope);
    }

    public void onclick(Function<MessageContext, String> onclick) {
        this.onclick = onclick;
    }

    @Override
    public void setValue(SettingPersistenceService persistence, Topic topic, String value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getValue(SettingPersistenceService persistence, Topic topic) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean set(MessageContext context, MessageGeneric message) {
        return false;
    }

    @Override
    public BlockComponent render(MessageContext context) {
        if (this.onclick != null) {
            String result = onclick.apply(context);
            if (result != null) {
                return MarkdownUI.block(MarkdownUI.text(result));
            }
        }
        return MarkdownUI.block();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends SettingBuilder<Builder> {

        private Function<MessageContext, String> onclick;

        public Builder onclick(Function<MessageContext, String> onclick) {
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
