package cc.rapidev.qqbot.common;

import cc.rapidev.qqbot.common.Intents.IntentValue;
import lombok.Getter;

import java.util.List;

/**
 * @author leibrother
 */
@Getter
public enum Events {

    // Framework Event
    STARTED(IntentValue.BOT),
    // QQBot Event
    GUILD_CREATE(IntentValue.GUILDS),// 当机器人加入新guild时
    GUILD_UPDATE(IntentValue.GUILDS),// 当guild资料发生变更时
    GUILD_DELETE(IntentValue.GUILDS),// 当机器人退出guild时
    CHANNEL_CREATE(IntentValue.GUILDS),// 当channel被创建时
    CHANNEL_UPDATE(IntentValue.GUILDS),// 当channel被更新时
    CHANNEL_DELETE(IntentValue.GUILDS),// 当channel被删除时

    GUILD_MEMBER_ADD(IntentValue.GUILD_MEMBERS),// 当成员加入时
    GUILD_MEMBER_UPDATE(IntentValue.GUILD_MEMBERS),// 当成员资料变更时
    GUILD_MEMBER_REMOVE(IntentValue.GUILD_MEMBERS),// 当成员被移除时;

    MESSAGE_CREATE(IntentValue.GUILD_MESSAGES),// 发送消息事件，代表频道内的全部消息，而不只是 at 机器人的消息。内容与 AT_MESSAGE_CREATE 相同
    MESSAGE_DELETE(IntentValue.GUILD_MESSAGES),// 删除（撤回）消息事件

    MESSAGE_REACTION_ADD(IntentValue.GUILD_MESSAGE_REACTIONS),// 为消息添加表情表态
    MESSAGE_REACTION_REMOVE(IntentValue.GUILD_MESSAGE_REACTIONS),// 为消息删除表情表态

    DIRECT_MESSAGE_CREATE(IntentValue.DIRECT_MESSAGE),// 当收到用户发给机器人的私信消息时
    DIRECT_MESSAGE_DELETE(IntentValue.DIRECT_MESSAGE),// 删除（撤回）消息事件

    C2C_MESSAGE_CREATE(IntentValue.GROUP_AND_C2C_EVENT),// 用户单聊发消息给机器人时候
    FRIEND_ADD(IntentValue.GROUP_AND_C2C_EVENT),// 用户添加使用机器人
    FRIEND_DEL(IntentValue.GROUP_AND_C2C_EVENT),// 用户删除机器人
    C2C_MSG_REJECT(IntentValue.GROUP_AND_C2C_EVENT),// 用户在机器人资料卡手动关闭"主动消息"推送
    C2C_MSG_RECEIVE(IntentValue.GROUP_AND_C2C_EVENT),// 用户在机器人资料卡手动开启"主动消息"推送开关
    GROUP_AT_MESSAGE_CREATE(IntentValue.GROUP_AND_C2C_EVENT),// 用户在群里@机器人时收到的消息
    GROUP_ADD_ROBOT(IntentValue.GROUP_AND_C2C_EVENT),// 机器人被添加到群聊
    GROUP_DEL_ROBOT(IntentValue.GROUP_AND_C2C_EVENT),// 机器人被移出群聊
    GROUP_MSG_REJECT(IntentValue.GROUP_AND_C2C_EVENT),// 群管理员主动在机器人资料页操作关闭通知
    GROUP_MSG_RECEIVE(IntentValue.GROUP_AND_C2C_EVENT),// 群管理员主动在机器人资料页操作开启通知

    INTERACTION_CREATE(IntentValue.INTERACTION),// 互动事件创建时

    MESSAGE_AUDIT_PASS(IntentValue.MESSAGE_AUDIT),// 互动事件创建时
    MESSAGE_AUDIT_REJECT(IntentValue.MESSAGE_AUDIT),// 消息审核不通过

    FORUM_THREAD_CREATE(IntentValue.FORUMS_EVENT),// 当用户创建主题时
    FORUM_THREAD_UPDATE(IntentValue.FORUMS_EVENT),// 当用户更新主题时
    FORUM_THREAD_DELETE(IntentValue.FORUMS_EVENT),// 当用户删除主题时
    FORUM_POST_CREATE(IntentValue.FORUMS_EVENT),// 当用户创建帖子时
    FORUM_POST_DELETE(IntentValue.FORUMS_EVENT),// 当用户删除帖子时
    FORUM_REPLY_CREATE(IntentValue.FORUMS_EVENT),// 当用户回复评论时
    FORUM_REPLY_DELETE(IntentValue.FORUMS_EVENT),// 当用户回复评论时
    FORUM_PUBLISH_AUDIT_RESULT(IntentValue.FORUMS_EVENT),// 当用户发表审核通过时

    AUDIO_START(IntentValue.AUDIO_ACTION),// 音频开始播放时
    AUDIO_FINISH(IntentValue.AUDIO_ACTION),// 音频播放结束时
    AUDIO_ON_MIC(IntentValue.AUDIO_ACTION),// 上麦时
    AUDIO_OFF_MIC(IntentValue.AUDIO_ACTION),// 下麦时

    AT_MESSAGE_CREATE(IntentValue.PUBLIC_GUILD_MESSAGES),// 当收到@机器人的消息时
    PUBLIC_MESSAGE_DELETE(IntentValue.PUBLIC_GUILD_MESSAGES);// 当频道的消息被删除时

    private final IntentValue intent;

    Events(IntentValue intent) {
        this.intent = intent;
    }

    public static final List<Events> messageCreateEvents = List.of(
            C2C_MESSAGE_CREATE,
            GROUP_AT_MESSAGE_CREATE,
            MESSAGE_CREATE,
            AT_MESSAGE_CREATE,
            DIRECT_MESSAGE_CREATE
    );

    public static final List<Events> messageDeleteEvents = List.of(
            MESSAGE_DELETE,
            PUBLIC_MESSAGE_DELETE,
            DIRECT_MESSAGE_DELETE
    );

    public boolean isMessageCreate() {
        return messageCreateEvents.contains(this);
    }

    public boolean isMessageDelete() {
        return messageDeleteEvents.contains(this);
    }

}
