package cc.rapidev.qqbot.extension.command;

import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * @author leibrother
 */
public record Keyword(String key, String description) {

    public Keyword(String key) {
        this(key, "");
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Keyword keyword = (Keyword) o;
        return Objects.equals(key, keyword.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }

    @Override
    public @NonNull String toString() {
        return this.key;
    }
}
