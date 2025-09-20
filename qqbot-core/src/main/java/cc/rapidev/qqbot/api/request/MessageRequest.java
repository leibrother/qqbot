package cc.rapidev.qqbot.api.request;

import cc.rapidev.qqbot.api.BotApi;
import cc.rapidev.qqbot.api.model.Message;
import cc.rapidev.qqbot.api.model.MessageMedia;
import cc.rapidev.qqbot.api.response.MessageMediaResponse;
import cc.rapidev.qqbot.api.response.MessageResponse;

import java.net.URI;

/**
 * 消息相关Api
 *
 * @author leibrother
 */
public class MessageRequest extends AbstractRequest {

    public MessageRequest(BotApi openApi) {
        super(openApi);
    }

    public MessageResponse toUser(String id, Message message) {
        URI uri = uri("/v2/users/{openid}/messages");
        return doPost(uri, message, MessageResponse.class);
    }

    public MessageResponse toGroup(String id, Message message) {
        return null;
    }

    public MessageResponse toDirect(String id, Message message) {
        return null;
    }

    public MessageResponse toChannel(String id, Message message) {
        return null;
    }

    public MessageMediaResponse toUserMedia(String id, MessageMedia media) {
        return null;
    }

    public MessageMediaResponse toGroupMedia(String id, MessageMedia media) {
        return null;
    }

}
