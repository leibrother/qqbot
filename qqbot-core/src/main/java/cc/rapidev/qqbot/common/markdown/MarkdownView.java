package cc.rapidev.qqbot.common.markdown;

import cc.rapidev.qqbot.api.model.Message;
import cc.rapidev.qqbot.common.markdown.component.Block;
import cc.rapidev.qqbot.common.markdown.component.BlockComponent;
import cc.rapidev.qqbot.common.utils.CastUtils;

/**
 * @author leibrother
 */
public abstract class MarkdownView<T extends MarkdownView<T>> {

    private final Block<BlockComponent> div = MarkdownUI.block();

    public T add(BlockComponent... components) {
        this.div.add(components);
        return CastUtils.cast(this);
    }

    public Message render() {
        String markdown = div.render().strip();
        return Message.markdown(markdown);
    }

}
