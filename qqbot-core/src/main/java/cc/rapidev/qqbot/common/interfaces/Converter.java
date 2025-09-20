package cc.rapidev.qqbot.common.interfaces;

/**
 * @author leibrother
 */
@FunctionalInterface
public interface Converter<O, T> {

    T convert(O origin);

}
