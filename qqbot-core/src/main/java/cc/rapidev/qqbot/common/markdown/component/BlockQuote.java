package cc.rapidev.qqbot.common.markdown.component;

import java.util.stream.IntStream;

/**
 * @author leibrother
 */
public class BlockQuote implements BlockComponent {

    private final int level;
    private final Block<Text> div = new Block<>();

    public BlockQuote() {
        this(1);
    }

    public BlockQuote(int level) {
        this.level = Math.max(1, level);
    }

    public BlockQuote add(Text... components) {
        this.div.add(components);
        return this;
    }

    @Override
    public String render() {
        StringBuilder builder = new StringBuilder();
        IntStream.range(0, this.level).forEach((_) -> builder.append(">"));
        builder.append(" ");
        builder.append(this.div.render());
        return builder.toString();
    }

}
