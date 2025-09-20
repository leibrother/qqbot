package cc.rapidev.qqbot.api.request;

import cc.rapidev.qqbot.api.BotApi;
import cc.rapidev.qqbot.api.model.User;
import cc.rapidev.qqbot.api.response.AccessTokenResponse;
import com.fasterxml.jackson.databind.JsonNode;

import java.net.URI;
import java.util.Map;

/**
 * @author leibrother
 */
public class AuthRequest extends AbstractRequest {

    public AuthRequest(BotApi openApi) {
        super(openApi);
    }

    /**
     * <p>获取AccessToken</p>
     * <p>文档：<a href="https://bot.q.qq.com/wiki/develop/api-v2/dev-prepare/interface-framework/api-use.html">接口调用与鉴权</a></p>
     *
     * @return 凭证与过期时间
     */
    public AccessTokenResponse getAccessToken() {
        URI uri = getRequestHelper().https("bots.qq.com", "/app/getAppAccessToken");
        Map<String, String> body = emptyMap();
        body.put("appId", getAppid());
        body.put("clientSecret", getSecret());
        JsonNode node = getRequestHelper().doPost(uri, null, body, JsonNode.class);
        return responseGet(node, AccessTokenResponse.class);
    }

    public User info() {
        URI uri = uri("/users/@me");
        return doGet(uri, User.class);
    }

}
