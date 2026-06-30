package cc.rapidev.qqbot.common.markdown;

import cc.rapidev.qqbot.api.model.Message;
import cc.rapidev.qqbot.common.markdown.component.Block;
import cc.rapidev.qqbot.common.markdown.component.BlockComponent;

/**
 * @author leibrother
 */
public abstract class MarkdownView {

    private final Block<BlockComponent> div = MarkdownUI.block();

    protected void add(BlockComponent... components) {
        this.div.add(components);
    }

    public Message render() {
        String markdown = div.render().strip();
        return Message.markdown(markdown);
    }

}
