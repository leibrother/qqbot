package cc.rapidev.qqbot.api.request;

import cc.rapidev.qqbot.api.BotRequest;
import cc.rapidev.qqbot.api.model.GuildMessage;
import cc.rapidev.qqbot.api.model.Message;
import cc.rapidev.qqbot.api.model.MessageMedia;
import cc.rapidev.qqbot.api.response.MessageMediaResponse;
import cc.rapidev.qqbot.api.response.MessageResponse;
import cc.rapidev.qqbot.common.request.ApiUrl;
import cc.rapidev.qqbot.common.utils.JsonUtils;
import com.fasterxml.jackson.databind.JsonNode;
import okhttp3.HttpUrl;

/**
 * 消息相关Api
 *
 * @author leibrother
 */
public class MessageRequest {

    private final BotRequest request;

    public MessageRequest(BotRequest request) {
        this.request = request;
    }

    public MessageResponse toUser(String id, Message message) {
        HttpUrl url = ApiUrl.create(request.baseUrl()).path("/v2/users/{0}/messages", id).build();
        JsonNode node = request.wrap().post(url, message);
        return JsonUtils.convert(node, MessageResponse.class);
    }

    public MessageResponse toGroup(String id, Message message) {
        HttpUrl url = ApiUrl.create(request.baseUrl()).path("/v2/groups/{0}/messages", id).build();
        JsonNode node = request.wrap().post(url, message);
        return JsonUtils.convert(node, MessageResponse.class);
    }

    public MessageResponse toDirect(String id, Message message) {
        return toDirect(id, GuildMessage.form(message));
    }

    public MessageResponse toDirect(String id, GuildMessage message) {
        HttpUrl url = ApiUrl.create(request.baseUrl()).path("/dms/{0}/messages", id).build();
        JsonNode node = request.wrap().post(url, message);
        return JsonUtils.convert(node, MessageResponse.class);
    }

    public MessageResponse toChannel(String id, Message message) {
        return toChannel(id, GuildMessage.form(message));
    }

    public MessageResponse toChannel(String id, GuildMessage message) {
        HttpUrl url = ApiUrl.create(request.baseUrl()).path("/channels/{0}/messages", id).build();
        JsonNode node = request.wrap().post(url, message);
        return JsonUtils.convert(node, MessageResponse.class);
    }

    public MessageMediaResponse toUserMedia(String id, MessageMedia media) {
        HttpUrl url = ApiUrl.create(request.baseUrl()).path("/v2/users/{0}/files", id).build();
        JsonNode node = request.wrap().post(url, media);
        return JsonUtils.convert(node, MessageMediaResponse.class);
    }

    public MessageMediaResponse toGroupMedia(String id, MessageMedia media) {
        HttpUrl url = ApiUrl.create(request.baseUrl()).path("/v2/groups/{0}/files", id).build();
        JsonNode node = request.wrap().post(url, media);
        return JsonUtils.convert(node, MessageMediaResponse.class);
    }

}
