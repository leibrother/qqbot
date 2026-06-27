package cc.rapidev.qqbot.common.markdown;

import cc.rapidev.qqbot.common.markdown.component.*;

/**
 * @author leibrother
 */
public class MarkdownUI {

    @SafeVarargs
    public static <T extends Component> Block<T> block(T... components) {
        return Block.of(components);
    }

    public static Text text(String text) {
        return new Text(text);
    }

    public static Text text(String text, boolean bold, boolean italic) {
        return new EmphasizedText(text).bold(bold).italic(italic);
    }

    public static Code code(String code) {
        return new Code(code);
    }

    public static BlockCode blockCode(String code) {
        return new BlockCode(code);
    }

    public static BlockQuote blockQuote(Text... components) {
        return new BlockQuote().add(components);
    }

    public static BlockQuote blockQuote(String text) {
        return blockQuote(text(text));
    }

    public static Heading h1(Text... components) {
        return new Heading(1).add(components);
    }

    public static Heading h1(String text) {
        return h1(text(text));
    }

    public static Heading h2(Text... components) {
        return new Heading(2).add(components);
    }

    public static Heading h2(String text) {
        return h2(text(text));
    }

    public static Heading h3(Text... components) {
        return new Heading(3).add(components);
    }

    public static Heading h3(String text) {
        return h3(text(text));
    }

    public static Heading h4(Text... components) {
        return new Heading(3).add(components);
    }

    public static Heading h4(String text) {
        return h4(text(text));
    }

    public static Heading h5(Text... components) {
        return new Heading(3).add(components);
    }

    public static Heading h5(String text) {
        return h5(text(text));
    }

    public static Heading h6(Text... components) {
        return new Heading(3).add(components);
    }

    public static Heading h6(String text) {
        return h6(text(text));
    }

    public static Separator separator() {
        return new Separator();
    }
}
