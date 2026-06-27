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
    private final CommandHelper helper;

    public CommandHandlerSet() {
        this(null);
    }

    public CommandHandlerSet(CommandHandler defaultHandler) {
        this.defaultHandler = defaultHandler;
        // 默认注册帮助命令
        this.helper = new CommandHelper();
        this.helper.register(this);
    }

    public void add(String key, CommandHandler handler, Event... events) {
        Keyword keyword = new Keyword(key);
        add(keyword, handler, events);
    }

    public void add(Keyword keyword, CommandHandler handler, Event... events) {
        if (this.mapping.containsKey(keyword)) {
            throw new IllegalArgumentException("Keyword is already registered");
        }
        this.mapping.put(keyword, handler);
        if (events != null && events.length > 0) {
            this.keyEvents.put(keyword, List.of(events));
        }
        logger.debug("registered '{}' to {}", keyword, handler.getClass().getName());
    }

    public List<Keyword> keywords(Event event) {
        return this.mapping.keySet().stream()
                .filter(keyword -> !this.keyEvents.containsKey(keyword) || this.keyEvents.get(keyword).contains(event))
                .toList();
    }

    @Override
    public void handle(MessageContext context, Command command) {
        if (command.isEmpty()) {
            this.helper.handle(context, command);
            return;
        }
        Event event = context.event();
        for (Keyword keyword : keywords(event)) {
            Optional<Command> next = command.match(keyword);
            if (next.isPresent()) {
                mapping.get(keyword).handle(context, next.get());
                return;
            }
        }
        if (defaultHandler != null) {
            defaultHandler.handle(context, command);
        }
    }

}
