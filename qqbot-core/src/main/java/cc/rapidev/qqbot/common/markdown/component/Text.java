package cc.rapidev.qqbot.common.markdown.component;

/**
 * @author leibrother
 */
public class Text implements Component {

    protected final String value;

    public Text(String value) {
        this.value = value;
    }

    @Override
    public String render() {
        return value;
    }

    public static Text of(String value) {
        return new Text(value);
    }

    public static Text whitespace() {
        return of(" ");
    }

}
