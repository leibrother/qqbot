package cc.rapidev.qqbot.common.markdown;

import cc.rapidev.qqbot.common.markdown.component.*;
import cc.rapidev.qqbot.common.markdown.interactive.CmdEnter;
import cc.rapidev.qqbot.common.markdown.interactive.CmdInput;

/**
 * @author leibrother
 */
public class MarkdownUI {

    @SafeVarargs
    public static <T extends Component> Block<T> block(T... components) {
        return Block.of(components);
    }

    public static Text whitespace() {
        return Text.whitespace();
    }

    public static Text text(String text) {
        return Text.of(text);
    }

    public static Code code(String code) {
        return new Code(code);
    }

    public static Bold bold(Text text) {
        return new Bold(text);
    }

    public static Bold bold(String text) {
        return new Bold(text);
    }

    public static Italic italic(Text text) {
        return new Italic(text);
    }

    public static Italic italic(String text) {
        return new Italic(text);
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

    public static Listview list() {
        return new Listview();
    }

    public static Item item(String text) {
        return item(text(text));
    }

    public static Item item(Text... components) {
        return new Item().add(components);
    }

    public static CmdEnter cmdEnter(String text) {
        return new CmdEnter(text);
    }

    public static CmdInput cmdInput(String text) {
        return new CmdInput(text);
    }

    public static CmdInput cmdInput(String text, String show, boolean reference) {
        return new CmdInput(text, show, reference);
    }

}
