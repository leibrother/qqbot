package cc.rapidev.qqbot.message.matcher;

import cc.rapidev.qqbot.message.MessageContext;
import cc.rapidev.qqbot.message.MessageHandler;

import java.util.List;

/**
 * @author leibrother
 */
public abstract class MatcherMessageHandler implements MessageHandler {

    public abstract List<String> keywords();

    public abstract void process(MessageContext context, MatcherService matcher);

    @Override
    public void handle(MessageContext context) {
        MatcherService matcher = context.getService(MatcherService.class).branch();
        if (matcher.match(keywords())) {
            this.process(context, matcher);
            context.complete();
        }
    }

}
