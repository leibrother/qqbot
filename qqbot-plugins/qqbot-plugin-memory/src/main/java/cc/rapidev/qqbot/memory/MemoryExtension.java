package cc.rapidev.qqbot.memory;

import cc.rapidev.qqbot.Bot;
import cc.rapidev.qqbot.common.Event;
import cc.rapidev.qqbot.extension.Extension;
import cc.rapidev.qqbot.memory.handler.MemoryHandler;
import cc.rapidev.qqbot.memory.handler.MemoryRememberHandler;
import cc.rapidev.qqbot.memory.repository.MemoryRepository;
import cc.rapidev.qqbot.memory.repository.SQLiteMemoryRepository;
import cc.rapidev.qqbot.message.MessageDispatcher;

import java.util.stream.Stream;

/**
 * @author leibrother
 */
public class MemoryExtension implements Extension {

    @Override
    public void ready(Bot bot) {
        MemoryRepository repository = new SQLiteMemoryRepository(bot.database());
        MemoryHandler handler = new MemoryHandler(repository);
        MemoryRememberHandler remember = new MemoryRememberHandler();
        MessageDispatcher dispatcher = bot.dispatcher();
        Stream.of(Event.values()).forEach(e -> {
            dispatcher.register(e, handler);
            dispatcher.register(e, remember);
        });
    }

    @Override
    public void destroy() {
    }

}
