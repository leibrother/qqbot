package cc.rapidev.qqbot.extension.command;

import cc.rapidev.qqbot.api.model.Message;
import cc.rapidev.qqbot.common.Event;
import cc.rapidev.qqbot.extension.command.view.HelpView;
import cc.rapidev.qqbot.message.MessageContext;

import java.util.List;

/**
 * @author leibrother
 */
public class CommandHelper implements CommandHandler, KeywordRegisterer {

    private final Keyword keyword;
    private CommandHandlerSet root;

    public CommandHelper() {
        this.keyword = new Keyword("帮助");
    }

    @Override
    public void register(CommandHandlerSet entry) {
        this.root = entry;
        entry.add(keyword, this);
    }

    @Override
    public void handle(MessageContext context, Command command) {
        if (this.root == null) {
            return;
        }
        Event event = context.event();
        List<Keyword> all = root.keywords(event);
        List<Keyword> keywords = all.stream().filter(keyword -> !keyword.equals(this.keyword)).toList();
        HelpView view = new HelpView(keywords);
        Message message = view.render();
        context.reply(message);
    }

}
