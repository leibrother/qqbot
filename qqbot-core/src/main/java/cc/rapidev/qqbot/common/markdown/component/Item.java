package cc.rapidev.qqbot.common.markdown.component;

/**
 * @author leibrother
 */
public class Item implements BlockComponent {

    private final boolean orderly;
    private final Block<Text> div = new Block<>();

    public Item() {
        this(false);
    }

    public Item(boolean orderly) {
        this.orderly = orderly;
    }

    public boolean isOrderly() {
        return orderly;
    }

    public Item add(Text... components) {
        div.add(components);
        return this;
    }

    @Override
    public String render() {
        StringBuilder builder = new StringBuilder();
        if (isOrderly()) {
            builder.append(". ");
        } else {
            builder.append("- ");
        }
        builder.append(div.render());
        return builder.toString();
    }
}
