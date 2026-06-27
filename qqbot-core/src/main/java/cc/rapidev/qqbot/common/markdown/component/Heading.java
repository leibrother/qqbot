package cc.rapidev.qqbot.common.markdown.component;

import java.util.stream.IntStream;

/**
 * @author leibrother
 */
public class Heading implements BlockComponent {

    private final int level;
    private final Block<Text> div = new Block<>();

    public Heading() {
        this(1);
    }

    public Heading(int level) {
        this.level = Math.max(1, level);
    }

    public Heading add(Text... components) {
        div.add(components);
        return this;
    }

    @Override
    public String render() {
        StringBuilder builder = new StringBuilder();
        IntStream.range(0, level).forEach((_) -> builder.append("#"));
        builder.append(" ");
        builder.append(div.render());
        return builder.toString();
    }

}
