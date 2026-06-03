package cc.rapidev.qqbot.message.extractor;

import cc.rapidev.qqbot.BotPayload;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author leibrother
 */
public abstract class Extractor<T> {

    public final T extract(BotPayload payload) {
        return switch (payload.e()) {
            case GUILD_CREATE -> byGuideCreate(payload.data());
            case GUILD_UPDATE -> byGuideUpdate(payload.data());
            case GUILD_DELETE -> byGuideDelete(payload.data());
            case CHANNEL_CREATE -> byChannelCreate(payload.data());
            case CHANNEL_UPDATE -> byChannelUpdate(payload.data());
            case CHANNEL_DELETE -> byChannelDelete(payload.data());
            case GUILD_MEMBER_ADD -> byGuildMemberAdd(payload.data());
            case GUILD_MEMBER_UPDATE -> byGuildMemberUpdate(payload.data());
            case GUILD_MEMBER_REMOVE -> byGuildMemberRemove(payload.data());
            case MESSAGE_CREATE -> byMessageCreate(payload.data());
            case MESSAGE_DELETE -> byMessageDelete(payload.data());
            case MESSAGE_REACTION_ADD -> byMessageReactionAdd(payload.data());
            case MESSAGE_REACTION_REMOVE -> byMessageReactionRemove(payload.data());
            case DIRECT_MESSAGE_CREATE -> byDirectMessageCreate(payload.data());
            case DIRECT_MESSAGE_DELETE -> byDirectMessageDelete(payload.data());
            case C2C_MESSAGE_CREATE -> byC2CMessageCreate(payload.data());
            case FRIEND_ADD -> byFriendAdd(payload.data());
            case FRIEND_DEL -> byFriendDel(payload.data());
            case C2C_MSG_REJECT -> byC2CMsgReject(payload.data());
            case C2C_MSG_RECEIVE -> byC2CMsgReceive(payload.data());
            case GROUP_AT_MESSAGE_CREATE -> byGroupAtMessageCreate(payload.data());
            case GROUP_ADD_ROBOT -> byGroupAddRobot(payload.data());
            case GROUP_DEL_ROBOT -> byGroupDelRobot(payload.data());
            case GROUP_MSG_REJECT -> byGroupMsgReject(payload.data());
            case GROUP_MSG_RECEIVE -> byGroupMsgReceive(payload.data());
            case INTERACTION_CREATE -> byInteractionCreate(payload.data());
            case MESSAGE_AUDIT_PASS -> byMessageAuditPass(payload.data());
            case MESSAGE_AUDIT_REJECT -> byMessageAuditReject(payload.data());
            case FORUM_THREAD_CREATE -> byForumThreadCreate(payload.data());
            case FORUM_THREAD_UPDATE -> byForumThreadUpdate(payload.data());
            case FORUM_THREAD_DELETE -> byForumThreadDelete(payload.data());
            case FORUM_POST_CREATE -> byForumPostCreate(payload.data());
            case FORUM_POST_DELETE -> byForumPostDelete(payload.data());
            case FORUM_REPLY_CREATE -> byForumReplyCreate(payload.data());
            case FORUM_REPLY_DELETE -> byForumReplyDelete(payload.data());
            case FORUM_PUBLISH_AUDIT_RESULT -> byForumPublishAuditResult(payload.data());
            case AUDIO_START -> byAudioStart(payload.data());
            case AUDIO_FINISH -> byAudioFinish(payload.data());
            case AUDIO_ON_MIC -> byAudioOnMic(payload.data());
            case AUDIO_OFF_MIC -> byAudioOffMic(payload.data());
            case AT_MESSAGE_CREATE -> byAtMessageCreate(payload.data());
            case PUBLIC_MESSAGE_DELETE -> byPublishMessageDelete(payload.data());
            default ->throw new IllegalArgumentException("unknown payload type: " + payload.e());
        };
    }

    protected T byGuideCreate(JsonNode data) {
        return null;
    }

    protected T byGuideUpdate(JsonNode data) {
        return null;
    }

    protected T byGuideDelete(JsonNode data) {
        return null;
    }

    protected T byChannelCreate(JsonNode data) {
        return null;
    }

    protected T byChannelUpdate(JsonNode data) {
        return null;
    }

    protected T byChannelDelete(JsonNode data) {
        return null;
    }

    protected T byGuildMemberAdd(JsonNode data) {
        return null;
    }

    protected T byGuildMemberUpdate(JsonNode data) {
        return null;
    }

    protected T byGuildMemberRemove(JsonNode data) {
        return null;
    }

    protected T byMessageCreate(JsonNode data) {
        return null;
    }

    protected T byMessageDelete(JsonNode data) {
        return null;
    }

    protected T byMessageReactionAdd(JsonNode data) {
        return null;
    }

    protected T byMessageReactionRemove(JsonNode data) {
        return null;
    }

    protected T byDirectMessageCreate(JsonNode data) {
        return null;
    }

    protected T byDirectMessageDelete(JsonNode data) {
        return null;
    }

    protected T byC2CMessageCreate(JsonNode data) {
        return null;
    }

    protected T byFriendAdd(JsonNode data) {
        return null;
    }

    protected T byFriendDel(JsonNode data) {
        return null;
    }

    protected T byC2CMsgReject(JsonNode data) {
        return null;
    }

    protected T byC2CMsgReceive(JsonNode data) {
        return null;
    }

    protected T byGroupAtMessageCreate(JsonNode data) {
        return null;
    }

    protected T byGroupAddRobot(JsonNode data) {
        return null;
    }

    protected T byGroupDelRobot(JsonNode data) {
        return null;
    }

    protected T byGroupMsgReject(JsonNode data) {
        return null;
    }

    protected T byGroupMsgReceive(JsonNode data) {
        return null;
    }

    protected T byInteractionCreate(JsonNode data) {
        return null;
    }

    protected T byMessageAuditPass(JsonNode data) {
        return null;
    }

    protected T byMessageAuditReject(JsonNode data) {
        return null;
    }

    protected T byForumThreadCreate(JsonNode data) {
        return null;
    }

    protected T byForumThreadUpdate(JsonNode data) {
        return null;
    }

    protected T byForumThreadDelete(JsonNode data) {
        return null;
    }

    protected T byForumPostCreate(JsonNode data) {
        return null;
    }

    protected T byForumPostDelete(JsonNode data) {
        return null;
    }

    protected T byForumReplyCreate(JsonNode data) {
        return null;
    }

    protected T byForumReplyDelete(JsonNode data) {
        return null;
    }

    protected T byForumPublishAuditResult(JsonNode data) {
        return null;
    }

    protected T byAudioStart(JsonNode data) {
        return null;
    }

    protected T byAudioFinish(JsonNode data) {
        return null;
    }

    protected T byAudioOnMic(JsonNode data) {
        return null;
    }

    protected T byAudioOffMic(JsonNode data) {
        return null;
    }

    protected T byAtMessageCreate(JsonNode data) {
        return null;
    }

    protected T byPublishMessageDelete(JsonNode data) {
        return null;
    }

}
