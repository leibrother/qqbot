package cc.rapidev.qqbot.message.member;

import cc.rapidev.qqbot.Bot;
import cc.rapidev.qqbot.common.Author;
import cc.rapidev.qqbot.common.Event;
import cc.rapidev.qqbot.common.Topic;
import cc.rapidev.qqbot.message.MessageContext;
import cc.rapidev.qqbot.message.MessageDispatcher;
import cc.rapidev.qqbot.message.MessageHandler;
import cc.rapidev.qqbot.message.MessageHandlerInjector;

import java.util.Arrays;

/**
 * @author leibrother
 */
public class MemberHandler implements MessageHandler, MessageHandlerInjector {

    @Override
    public int order() {
        return 0;
    }

    @Override
    public void handle(MessageContext context) {
        Topic topic = context.topic();
        Author author = context.author();
        if (topic == null || author == null) {
            return;
        }
        Member member = new Member(topic, author);
        context.use(MemberService.class).store(member);
    }

    @Override
    public void inject(MessageDispatcher dispatcher) {
        Bot bot = dispatcher.getBot();
        MemberService service = new MemberService(bot);
        bot.add(service);
        Arrays.stream(Event.values())
                .forEach(event -> dispatcher.register(event, this));
    }

}
