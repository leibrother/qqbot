package cc.rapidev.qqbot.rocokingdom.command;

import cc.rapidev.qqbot.api.model.Message;
import cc.rapidev.qqbot.common.request.Requester;
import cc.rapidev.qqbot.extension.command.Command;
import cc.rapidev.qqbot.extension.command.CommandHandler;
import cc.rapidev.qqbot.message.MessageContext;
import cc.rapidev.qqbot.rocokingdom.view.FarawayMerchantView;
import com.fasterxml.jackson.databind.JsonNode;
import okhttp3.HttpUrl;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Iterator;

/**
 * @author leibrother
 */
public class FarawayMerchantHandler implements CommandHandler {

    private final Requester request;

    public FarawayMerchantHandler() {
        this.request = Requester.getInstance();
    }

    @Override
    public void handle(MessageContext context, Command command) {
        LocalDateTime now = LocalDateTime.now();
        if (now.toLocalTime().isBefore(LocalTime.of(8, 0))) {
            context.reply(Message.text("远行商人已经休息了哦，请早上8点后再来吧~"));
            return;
        }
        JsonNode data = data();
        Iterator<JsonNode> rounds = data.get("rounds").elements();
        while (rounds.hasNext()) {
            JsonNode round = rounds.next();
            LocalDateTime startTime = Instant.ofEpochMilli(round.get("startTime").longValue()).atZone(ZoneOffset.of("+8")).toLocalDateTime();
            LocalDateTime endTime = Instant.ofEpochMilli(round.get("endTime").longValue()).atZone(ZoneOffset.of("+8")).toLocalDateTime();
            if (now.isAfter(startTime) && now.isBefore(endTime)) {
                FarawayMerchantView view = new FarawayMerchantView(round);
                context.reply(view.render());
                return;
            }
        }
        context.reply(Message.text("远行商人数据未更新，请稍等片刻"));
    }

    private JsonNode data() {
        HttpUrl url = request.https("static.gamecenter.qq.com", "/game_tool/dynamic_backend_data/1110613799-faraway-merchant.json");
        return request.json().get(url);
    }

}
