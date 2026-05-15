package cc.rapidev.qqbot.adapter.webhook.handler;

import cc.rapidev.qqbot.BotPayload;
import cc.rapidev.qqbot.adapter.webhook.RequestVerify;
import cc.rapidev.qqbot.adapter.webhook.WebhookBotAdapter;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashMap;
import java.util.Map;

/**
 * @author leibrother
 */
public class WebhookOpCode13Handler extends WebhookHandler {

    public WebhookOpCode13Handler(WebhookBotAdapter botAdapter) {
        super(botAdapter);
    }

    @Override
    public Object handle(Map<String, Object> headers, BotPayload payload) {
        String secret = getConfig().getSecret();
        JsonNode data = payload.data(JsonNode.class);
        String timestamp = data.get("event_ts").asText();
        String token = data.get("plain_token").asText();
        String signature = RequestVerify.verify(secret, timestamp, token);
        Map<String, String> map = new HashMap<>();
        map.put("plain_token", token);
        map.put("signature", signature);
        return map;
    }

}
