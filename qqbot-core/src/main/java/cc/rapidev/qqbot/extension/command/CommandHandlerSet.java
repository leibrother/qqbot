package cc.rapidev.qqbot.extension.command;

import cc.rapidev.qqbot.common.Event;
import cc.rapidev.qqbot.message.MessageContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author leibrother
 */
public class CommandHandlerSet implements CommandHandler {

    private final Logger logger = LoggerFactory.getLogger(CommandHandlerSet.class);
    private final Map<Keyword, CommandHandler> mapping = new HashMap<>();
    private final Map<Keyword, List<Event>> keyEvents = new HashMap<>();
    private final CommandHandler defaultHandler;

    public CommandHandlerSet() {
        this(null);
    }

    public CommandHandlerSet(CommandHandler defaultHandler) {
        this.defaultHandler = defaultHandler;
    }

    public void add(String key, CommandHandler handler, Event... events) {
        Keyword keyword = new Keyword(key);
        add(keyword, handler, events);
    }

    public void add(Keyword keyword, CommandHandler handler, Event... events) {
        if (this.mapping.containsKey(keyword)) {
            throw new IllegalArgumentException("Keyword is already registered");
        }
        logger.debug("registered '{}' to {}", keyword, handler.getClass().getName());
        this.mapping.put(keyword, handler);
        if (events != null && events.length > 0) {
            this.keyEvents.put(keyword, List.of(events));
        }
    }

    @Override
    public void handle(MessageContext context, Command command) {
        Event event = context.event();
        for (Keyword keyword : mapping.keySet()) {
            if (!keyEvents.containsKey(keyword) || keyEvents.get(keyword).contains(event)) {
                Optional<Command> next = command.match(keyword);
                if (next.isEmpty()) {
                    continue;
                }
                mapping.get(keyword).handle(context, next.get());
                return;
            }
        }
        if (defaultHandler != null) {
            defaultHandler.handle(context, command);
        }
    }

}
