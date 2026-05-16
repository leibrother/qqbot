package cc.rapidev.qqbot.message.command;

import cc.rapidev.qqbot.common.Events;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Objects;

/**
 * @author leibrother
 */
public record Keyword(String key, String description, List<Events> events) {

    public Keyword(String key) {
        this(key, "", List.of());
    }

    public Keyword(String key, String description) {
        this(key, description, List.of());
    }

    public Keyword(String key, Events... events) {
        this(key, "", List.of(events));
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
