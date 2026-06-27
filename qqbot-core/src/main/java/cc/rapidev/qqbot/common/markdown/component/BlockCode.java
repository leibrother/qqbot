package cc.rapidev.qqbot.common.markdown.component;

/**
 * @author leibrother
 */
public class BlockCode extends Code implements BlockComponent {

    private final String lang;

    public BlockCode(String value) {
        this(null, value);
    }

    public BlockCode(String lang, String value) {
        super(value);
        this.lang = lang;
    }

    @Override
    public String render() {
        StringBuilder builder = new StringBuilder();
        builder.append("```");
        if (this.lang != null) {
            builder.append(this.lang);
        }
        builder.append("\n");
        builder.append(this.value);
        builder.append("\n");
        builder.append("```");
        builder.append("\n");
        return builder.toString();
    }

}
