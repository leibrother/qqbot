package cc.rapidev.qqbot.api.request;

import cc.rapidev.qqbot.api.BotApi;
import cc.rapidev.qqbot.api.RequestHelper;
import cc.rapidev.qqbot.common.utils.JsonUtils;
import cc.rapidev.qqbot.exception.BotRequestException;
import com.fasterxml.jackson.databind.JsonNode;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * @author leibrother
 */
public abstract class AbstractRequest {

    protected final BotApi openApi;

    public AbstractRequest(BotApi openApi) {
        this.openApi = openApi;
    }

    protected String getHost() {
        return openApi.getBot().getConfig().getHost();
    }

    protected String getAppid() {
        return openApi.getBot().getConfig().getAppid();
    }

    protected String getSecret() {
        return openApi.getBot().getConfig().getSecret();
    }

    protected RequestHelper getRequestHelper() {
        return openApi.getRequestHelper();
    }

    protected URI uri(String path) {
        return getRequestHelper().https(getHost(), path);
    }

    protected URI uri(String path, Map<String, String> parameters) {
        return getRequestHelper().https(getHost(), path, parameters);
    }

    protected Map<String, String> emptyMap() {
        return new HashMap<>();
    }

    protected Map<String, String> headers() {
        String accessToken = openApi.getAccessToken();
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Framework", "Rapidev-QQBot");
        headers.put("Authorization", "QQBot " + accessToken);
        return headers;
    }

    protected  <T> T responseGet(JsonNode response, Class<T> clazz) {
        int code = 0;
        if (response.has("code")) {
            code = response.get("code").asInt();
        }
        if (code != 0) {
            String message = response.get("message").asText();
            throw new BotRequestException(code, message);
        }
        return JsonUtils.convert(response, clazz);
    }

    protected <T> T doGet(URI uri, Class<T> clazz) {
        JsonNode response = getRequestHelper().doGet(uri, headers(), JsonNode.class);
        return responseGet(response, clazz);
    }

    protected <T> T doPost(URI uri, Object body, Class<T> clazz) {
        JsonNode response = getRequestHelper().doPost(uri, headers(), body, JsonNode.class);
        return responseGet(response, clazz);
    }

}
