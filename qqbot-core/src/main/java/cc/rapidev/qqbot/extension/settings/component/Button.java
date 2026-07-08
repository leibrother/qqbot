package cc.rapidev.qqbot.extension.settings.component;

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

    private final Function<MessageContext, String> onclick;

    public Button(Builder builder) {
        super(builder);
        this.onclick = builder.onclick;
    }

    @Override
    public void setValue(SettingPersistenceService persistence, Topic topic, String value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getValue(SettingPersistenceService persistence, Topic topic) {
        return null;
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

    public static class Builder extends SettingItemBuilder<Builder> {

        private Function<MessageContext, String> onclick;

        public Builder onclick(Function<MessageContext, String> onclick) {
            this.onclick = onclick;
            return this;
        }

        public Button build() {
            return new Button(this);
        }

    }

}
