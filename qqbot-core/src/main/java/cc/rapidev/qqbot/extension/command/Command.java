package cc.rapidev.qqbot.extension.command;

import cc.rapidev.qqbot.common.utils.StringUtils;

import java.util.Optional;

/**
 * @author leibrother
 */
public class Command {

    private final Command prev;
    private final String content;
    private final boolean ignoreCase;

    public Command(String content) {
        this(content, true);
    }

    public Command(String content, boolean ignoreCase) {
        String trimmed = content.trim();
        if (trimmed.startsWith("/")) {
            trimmed = trimmed.substring(1).trim();
        }
        this.prev = null;
        this.ignoreCase = ignoreCase;
        this.content = ignoreCase ? trimmed.toLowerCase() : trimmed;
    }

    private Command(Command prev, String content) {
        this.prev = prev;
        this.ignoreCase = prev.ignoreCase;
        this.content = content;
    }

    public Command prev() {
        return this.prev;
    }

    public String content() {
        return this.content;
    }

    public boolean isEmpty() {
        return StringUtils.isEmpty(this.content);
    }

    public synchronized Optional<Command> match(Keyword keyword) {
        return match(keyword.key());
    }

    public synchronized Optional<Command> match(String key) {
        if (ignoreCase) {
            key = key.toLowerCase();
        }
        if (content.startsWith(key)) {
            String leftover = content.substring(key.length()).trim();
            Command next = new Command(this, leftover);
            return Optional.of(next);
        }
        return Optional.empty();
    }

}
