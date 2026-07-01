package cc.rapidev.qqbot.extension.settings.manager;

import cc.rapidev.qqbot.common.Event;
import cc.rapidev.qqbot.message.MessageContext;
import cc.rapidev.qqbot.message.MessageDispatcher;
import cc.rapidev.qqbot.message.MessageHandler;
import cc.rapidev.qqbot.message.MessageHandlerInjector;
import cc.rapidev.qqbot.message.member.Member;
import cc.rapidev.qqbot.message.member.MemberService;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Optional;

/**
 * @author leibrother
 */
public class ManagerHandler implements MessageHandler, MessageHandlerInjector {

    private final ManagerService service;

    public ManagerHandler(ManagerService service) {
        this.service = service;
    }

    @Override
    public int order() {
        return 1;
    }

    @Override
    public void handle(MessageContext context) {
        switch (context.event()) {
            case GUILD_CREATE -> {
                JsonNode data = context.payload().data();
                String id = data.get("op_user_id").textValue();
                Optional<Member> optional = context.use(MemberService.class).findById(context.topic(), id);
                optional.ifPresent(member -> service.add(context, member, true));
            }
            case GROUP_ADD_ROBOT -> {
                JsonNode data = context.payload().data();
                String openid = data.get("op_member_openid").textValue();
                Optional<Member> optional = context.use(MemberService.class).findByOpenid(context.topic(), openid);
                optional.ifPresent(member -> service.add(context, member, true));
            }
            case GUILD_DELETE, GROUP_DEL_ROBOT -> service.reset(context);
        }
    }

    @Override
    public void inject(MessageDispatcher dispatcher) {
        dispatcher.register(Event.GROUP_ADD_ROBOT, this);
        dispatcher.register(Event.GROUP_DEL_ROBOT, this);
        dispatcher.register(Event.GUILD_CREATE, this);
        dispatcher.register(Event.GUILD_DELETE, this);
    }

}
