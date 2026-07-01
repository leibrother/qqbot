package cc.rapidev.qqbot.extension.settings.manager;

import cc.rapidev.qqbot.api.model.Message;
import cc.rapidev.qqbot.common.Scope;
import cc.rapidev.qqbot.common.Topic;
import cc.rapidev.qqbot.common.markdown.MarkdownUI;
import cc.rapidev.qqbot.common.markdown.component.Block;
import cc.rapidev.qqbot.common.markdown.component.BlockComponent;
import cc.rapidev.qqbot.common.markdown.component.Component;
import cc.rapidev.qqbot.common.markdown.component.Listview;
import cc.rapidev.qqbot.common.utils.StringUtils;
import cc.rapidev.qqbot.extension.command.Command;
import cc.rapidev.qqbot.extension.command.CommandHandlerSet;
import cc.rapidev.qqbot.extension.settings.component.SettingItem;
import cc.rapidev.qqbot.message.MessageContext;
import cc.rapidev.qqbot.message.member.Member;
import cc.rapidev.qqbot.message.member.MemberService;
import cc.rapidev.qqbot.message.model.MessageGeneric;

import java.util.List;

/**
 * @author leibrother
 */
public class ManagerSettingItem extends SettingItem {

    private final CommandHandlerSet handler;

    public ManagerSettingItem(String key, String name, String description) {
        super(key, name, description, Scope.TOPIC);
        this.handler = new CommandHandlerSet(this::add);
        this.handler.add("移除", this::remove);
    }

    @Override
    public String getViewValue(MessageContext context) {
        return null;
    }

    @Override
    public boolean set(MessageContext context, MessageGeneric message) {
        String content = context.message().content().trim();
        handler.handle(context, new Command(content));
        return false;
    }

    @Override
    public BlockComponent render(MessageContext context) {
        Block<Component> block = MarkdownUI.block();
        block.add(MarkdownUI.block(MarkdownUI.blockQuote("发送成员昵称添加管理员")));
        List<String> ids = getManagerMemberIds(context);
        List<Member> members = context.use(MemberService.class).list(context.topic())
                .stream()
                .filter(member -> ids.contains(member.id()))
                .toList();
        if (members.isEmpty()) {
            block.add(MarkdownUI.text("无管理员"));
            return block;
        }
        Listview list = MarkdownUI.list();
        for (Member member : members) {
            list.add(
                    MarkdownUI.item(
                            MarkdownUI.bold(StringUtils.orDefault(member.username(), member.id().substring(0, 8))),
                            MarkdownUI.whitespace(),
                            MarkdownUI.cmdInput("移除 " + member.id(), "移除", false)
                    )
            );
        }
        return block.add(list);
    }

    public List<String> getManagerMemberIds(MessageContext context) {
        Topic topic = context.topic();
        if (topic.isPrivate() || topic.isDirect()) {
            return List.of(context.author().id());
        }
        String value = super.getValue(context);
        if (StringUtils.isEmpty(value)) {
            return List.of();
        }
        return List.of(value.split(","));
    }

    public boolean isManager(MessageContext context) {
        List<String> ids = getManagerMemberIds(context);
        return ids.contains(context.author().id());
    }

    private void add(MessageContext context, Command command) {
        if (!isManager(context)) {
            context.reply(Message.text("无权操作"));
        }
    }

    private void remove(MessageContext context, Command command) {
        if (!isManager(context)) {
            context.reply(Message.text("无权操作"));
        }
    }

}
