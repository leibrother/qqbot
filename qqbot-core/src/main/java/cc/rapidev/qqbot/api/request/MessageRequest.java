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
        URI uri = uri("/v2/users/{0}/messages", id);
        return doPost(uri, message, MessageResponse.class);
    }

    public MessageResponse toGroup(String id, Message message) {
        URI uri = uri("/v2/groups/{0}/messages", id);
        return doPost(uri, message, MessageResponse.class);
    }

    public MessageResponse toDirect(String id, Message message) {
        URI uri = uri("/dms/{0}/messages", id);
        return doPost(uri, message, MessageResponse.class);
    }

    public MessageResponse toChannel(String id, Message message) {
        URI uri = uri("/channels/{0}/messages", id);
        return doPost(uri, message, MessageResponse.class);
    }

    public MessageMediaResponse toUserMedia(String id, MessageMedia media) {
        URI uri = uri("/v2/users/{0}/files", id);
        return doPost(uri, media, MessageMediaResponse.class);
    }

    public MessageMediaResponse toGroupMedia(String id, MessageMedia media) {
        URI uri = uri("/v2/groups/{0}/files", id);
        return doPost(uri, media, MessageMediaResponse.class);
    }

}
