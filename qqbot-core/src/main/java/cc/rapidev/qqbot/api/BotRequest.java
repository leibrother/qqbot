package cc.rapidev.qqbot.api;

import cc.rapidev.qqbot.Bot;
import cc.rapidev.qqbot.api.model.User;
import cc.rapidev.qqbot.api.request.MessageRequest;
import cc.rapidev.qqbot.api.response.AccessTokenResponse;
import cc.rapidev.qqbot.common.request.Requester;
import cc.rapidev.qqbot.common.request.Wrap;
import cc.rapidev.qqbot.common.request.wrapper.JsonRequestWrapper;
import cc.rapidev.qqbot.common.utils.JsonUtils;
import com.fasterxml.jackson.databind.JsonNode;
import okhttp3.*;
import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author leibrother
 */
public class BotRequest {

    private final Bot bot;
    private final Requester requester;
    private AccessTokenResponse tokenResponse;

    public BotRequest(Bot bot) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public @NonNull Response intercept(Interceptor.@NonNull Chain chain) throws IOException {
                        if (chain.request().url().host().equals(host())) {
                            Request request = chain.request().newBuilder()
                                    .addHeader("Authorization", "QQBot " + token())
                                    .build();
                            return chain.proceed(request);
                        }
                        return chain.proceed(chain.request());
                    }
                })
                .build();
        this.bot = bot;
        this.requester = new Requester(client);
        bot.add(new MessageRequest(this));
    }

    public Requester requester() {
        return this.requester;
    }

    public String host() {
        return this.bot.getConfig().getHost();
    }

    public String baseUrl() {
        return "https://" + host();
    }

    public Wrap<JsonRequestWrapper, JsonNode> wrap() {
        return this.requester.json();
    }

    public synchronized String token() {
        if (this.tokenResponse != null && !this.tokenResponse.isExpire()) {
            return this.tokenResponse.getAccessToken();
        }
        HttpUrl url = requester.https("bots.qq.com", "/app/getAppAccessToken");
        Map<String, Object> map = new HashMap<>();
        map.put("appId", bot.getConfig().getAppid());
        map.put("clientSecret", bot.getConfig().getSecret());
        JsonNode response = wrap().post(url, map);
        this.tokenResponse = JsonUtils.convert(response, AccessTokenResponse.class);
        return this.tokenResponse.getAccessToken();
    }

    public User info() {
        HttpUrl url = requester.https(host(), "/users/@me");
        JsonNode node = wrap().get(url);
        return JsonUtils.convert(node, User.class);
    }

}
