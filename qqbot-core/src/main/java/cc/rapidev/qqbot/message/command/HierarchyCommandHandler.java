package cc.rapidev.qqbot.message.command;

import cc.rapidev.qqbot.common.Events;
import cc.rapidev.qqbot.message.MessageContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author leibrother
 */
public class HierarchyCommandHandler implements CommandHandler {

    private final Logger logger = LoggerFactory.getLogger(HierarchyCommandHandler.class);
    private final Map<Keyword, CommandHandler> mapping = new HashMap<>();

    public void register(String key, CommandHandler handler) {
        Keyword keyword = new Keyword(key);
        register(keyword, handler);
    }

    public void register(Keyword keyword, CommandHandler handler) {
        if (this.mapping.containsKey(keyword)) {
            throw new IllegalArgumentException("Keyword is already registered");
        }
        logger.debug("registered '{}' to {}", keyword, handler.getClass().getName());
        this.mapping.put(keyword, handler);
    }

    @Override
    public void handle(MessageContext context, Command command) {
        Events event = context.event();
        for (Keyword keyword : mapping.keySet()) {
            if (keyword.events().isEmpty() || keyword.events().contains(event)) {
                Optional<Command> next = command.match(keyword);
                if (next.isEmpty()) {
                    continue;
                }
                mapping.get(keyword).handle(context, next.get());
                break;
            }
        }
    }

}
