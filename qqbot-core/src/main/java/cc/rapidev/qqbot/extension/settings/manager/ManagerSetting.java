package cc.rapidev.qqbot.extension.settings.manager;

import cc.rapidev.qqbot.api.model.Message;
import cc.rapidev.qqbot.common.Scope;
import cc.rapidev.qqbot.common.Topic;
import cc.rapidev.qqbot.common.markdown.MarkdownUI;
import cc.rapidev.qqbot.common.markdown.component.*;
import cc.rapidev.qqbot.common.utils.JsonUtils;
import cc.rapidev.qqbot.common.utils.StringUtils;
import cc.rapidev.qqbot.extension.command.Command;
import cc.rapidev.qqbot.extension.command.CommandHandlerSet;
import cc.rapidev.qqbot.extension.settings.SettingView;
import cc.rapidev.qqbot.extension.settings.component.SettingItem;
import cc.rapidev.qqbot.extension.settings.manager.view.ManagerCandidateView;
import cc.rapidev.qqbot.message.MessageContext;
import cc.rapidev.qqbot.message.member.Member;
import cc.rapidev.qqbot.message.member.MemberService;
import cc.rapidev.qqbot.message.model.MessageGeneric;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author leibrother
 */
public class ManagerSetting extends SettingItem {

    private final CommandHandlerSet handler;

    public ManagerSetting(String key, String name, String description) {
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
        Map<String, Manager> managers = getManagerList(context).stream()
                .collect(Collectors.toUnmodifiableMap(Manager::openid, Function.identity()));
        List<Member> members = context.use(MemberService.class).list(context.topic())
                .stream()
                .filter(member -> managers.containsKey(member.id()))
                .toList();
        if (members.isEmpty()) {
            block.add(MarkdownUI.text("无管理员"));
            return block;
        }
        String me = context.author().openid();
        boolean iAmSupermanager = managers.containsKey(me) && managers.get(me).supermanager();
        Listview list = MarkdownUI.list();
        for (Member member : members) {
            Item item = MarkdownUI.item();
            boolean my = member.openid().equals(me);
            boolean supermanager = managers.get(member.openid()).supermanager();
            item.add(MarkdownUI.bold(StringUtils.orDefault(member.username(), member.id().substring(0, 8))));
            if (supermanager) {
                item.add(MarkdownUI.whitespace(), MarkdownUI.text("*"));
            } else if (iAmSupermanager && !my) {
                item.add(MarkdownUI.whitespace());
                item.add(MarkdownUI.cmdInput("移除 " + member.id(), "移除", false));
            }
            list.add(item);
        }
        return block.add(list);
    }

    private void setManagerList(MessageContext context, List<Manager> managerList) {
        if (managerList == null || managerList.isEmpty()) {
            super.setValue(context, null);
        } else {
            String value = JsonUtils.toJson(managerList);
            super.setValue(context, value);
        }
    }

    private List<Manager> getManagerList(MessageContext context) {
        Topic topic = context.topic();
        if (topic.isPrivate() || topic.isDirect()) {
            String openid = context.author().openid();
            return List.of(new Manager(openid, true));
        }
        String value = super.getValue(context);
        if (StringUtils.isEmpty(value)) {
            return List.of();
        }
        return JsonUtils.fromJson(value, new TypeReference<>() {
        });
    }

    public boolean isManager(MessageContext context) {
        return isManager(context, new Member(context.topic(), context.author()));
    }

    public boolean isManager(MessageContext context, Member member) {
        String openid = member.openid();
        List<Manager> list = getManagerList(context);
        return list.stream().map(Manager::openid).toList().contains(openid);
    }

    public boolean isSupermanager(MessageContext context) {
        return isSupermanager(context, new Member(context.topic(), context.author()));
    }

    public boolean isSupermanager(MessageContext context, Member member) {
        String openid = member.openid();
        List<Manager> list = getManagerList(context);
        return list.stream().filter(Manager::supermanager).map(Manager::openid).toList().contains(openid);
    }

    public void add(MessageContext context, Member member, boolean supermanager) {
        List<Manager> list = new ArrayList<>(getManagerList(context));
        list.removeIf(manager -> manager.openid().equals(member.openid()));
        list.add(new Manager(member.openid(), supermanager));
        this.setManagerList(context, list);
    }

    public void remove(MessageContext context, Member member) {
        List<Manager> list = new ArrayList<>(getManagerList(context));
        list.removeIf(manager -> manager.openid().equals(member.openid()));
        this.setManagerList(context, list);
    }

    public void reset(MessageContext context) {
        this.setManagerList(context, List.of());
    }

    private void add(MessageContext context, Command command) {
        if (!isSupermanager(context)) {
            context.reply(Message.text("无权操作: 你不是超级管理员"));
            return;
        }
        String key = command.content().trim().toUpperCase();
        List<Member> members = context.use(MemberService.class).list(context.topic());
        Optional<Member> optional = members.stream().filter(member -> member.openid().equals(key)).findAny();
        if (optional.isPresent()) {
            this.add(context, optional.get(), false);
            SettingView view = new SettingView(this, context);
            context.reply(view.render());
            return;
        }
        List<Member> candidate = members.stream().filter(member -> member.username().toUpperCase().contains(key)).toList();
        if (candidate.isEmpty()) {
            context.reply(Message.text("未找到匹配的成员"));
        } else if (candidate.size() == 1) {
            this.add(context, members.getFirst(), false);
            SettingView view = new SettingView(this, context);
            context.reply(view.render());
        } else {
            ManagerCandidateView view = new ManagerCandidateView(members);
            context.reply(view.render());
        }
    }

    private void remove(MessageContext context, Command command) {
        if (!isSupermanager(context)) {
            context.reply(Message.text("无权操作: 你不是超级管理员"));
            return;
        }
        String openid = command.content().trim();
        Optional<Member> optional = context.use(MemberService.class).findById(context.topic(), openid);
        if (optional.isEmpty()) {
            context.reply(Message.text("未知的成员"));
            return;
        }
        Member member = optional.get();
        if (member.id().equals(context.author().id())) {
            context.reply(Message.text("不能移除自己"));
            return;
        }
        if (isSupermanager(context, member)) {
            context.reply(Message.text("不能移除超级管理员"));
            return;
        }
        remove(context, member);
        SettingView view = new SettingView(this, context);
        context.reply(view.render());
    }

}
