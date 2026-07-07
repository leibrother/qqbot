package cc.rapidev.qqbot.extension.command;

import cc.rapidev.qqbot.common.Event;
import cc.rapidev.qqbot.extension.admin.AdminService;
import cc.rapidev.qqbot.extension.command.admin.AdminCommandHandler;
import cc.rapidev.qqbot.extension.command.helper.HelperHandler;
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
    private final Map<Keyword, List<Event>> keyEventsMapping = new HashMap<>();
    private final Map<Keyword, CommandHandler> keyHandlerMapping = new HashMap<>();
    private final CommandHandler defaultHandler;

    public CommandHandlerSet() {
        HelperHandler helper = new HelperHandler();
        this(helper);
        helper.register(this);
    }

    public CommandHandlerSet(CommandHandler defaultHandler) {
        this.defaultHandler = defaultHandler;
    }

    public void add(String key, CommandHandler handler, Event... events) {
        Keyword keyword = new Keyword(key);
        add(keyword, handler, events);
    }

    public void add(Keyword keyword, CommandHandler handler, Event... events) {
        if (this.keyHandlerMapping.containsKey(keyword)) {
            throw new IllegalArgumentException("Keyword is already registered");
        }
        this.keyHandlerMapping.put(keyword, handler);
        if (events != null && events.length > 0) {
            this.keyEventsMapping.put(keyword, List.of(events));
        }
        logger.debug("registered '{}' to {}", keyword, handler.getClass().getName());
    }

    public List<Keyword> keywords(MessageContext context) {
        Event event = context.event();
        List<Keyword> keywords = this.keyHandlerMapping.keySet().stream()
                .filter(keyword -> !this.keyEventsMapping.containsKey(keyword) || this.keyEventsMapping.get(keyword).contains(event))
                .toList();
        boolean admin = context.use(AdminService.class).isAdmin(context.author());
        if (!admin) {
            keywords = keywords.stream()
                    .filter(keyword -> !(this.keyHandlerMapping.get(keyword) instanceof AdminCommandHandler))
                    .toList();
        }
        return keywords;
    }

    @Override
    public void handle(MessageContext context, Command command) {
        if (command.isEmpty() && defaultHandler != null) {
            this.defaultHandler.handle(context, command);
            return;
        }
        for (Keyword keyword : keywords(context)) {
            Optional<Command> next = command.match(keyword);
            if (next.isPresent()) {
                keyHandlerMapping.get(keyword).handle(context, next.get());
                return;
            }
        }
        if (defaultHandler != null) {
            defaultHandler.handle(context, command);
        }
    }

}
