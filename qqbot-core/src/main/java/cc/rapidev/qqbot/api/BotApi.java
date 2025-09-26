package cc.rapidev.qqbot.api;

import cc.rapidev.qqbot.Bot;
import cc.rapidev.qqbot.api.request.AuthRequest;
import cc.rapidev.qqbot.api.request.MessageRequest;
import cc.rapidev.qqbot.api.response.AccessTokenResponse;
import lombok.Getter;

/**
 * 机器人接口管理类
 * <p>所有的机器人接口都需要在此实例化</p>
 *
 * @author leibrother
 */
public class BotApi {

    @Getter
    private final Bot bot;
    @Getter
    private final RequestHelper requestHelper;
    @Getter
    private AuthRequest authRequest;
    @Getter
    private MessageRequest messageRequest;

    public BotApi(Bot bot) {
        this.bot = bot;
        this.requestHelper = new RequestHelper();
        this.init();
    }

    private void init() {
        this.authRequest = new AuthRequest(this);
        this.messageRequest = new MessageRequest(this);
    }

    private AccessTokenResponse accessTokenResponse;

    public String getAccessToken() {
        if (accessTokenResponse != null && !accessTokenResponse.isExpire()) {
            return accessTokenResponse.getAccessToken();
        }
        accessTokenResponse = this.authRequest.getAccessToken();
        return accessTokenResponse.getAccessToken();
    }

}
