package cc.rapidev.qqbot.rocokingdom.command;

import cc.rapidev.qqbot.Bot;
import cc.rapidev.qqbot.api.model.Message;
import cc.rapidev.qqbot.common.request.Requester;
import cc.rapidev.qqbot.extension.command.Command;
import cc.rapidev.qqbot.extension.command.CommandHandler;
import cc.rapidev.qqbot.message.MessageContext;
import cc.rapidev.qqbot.rocokingdom.view.FarawayMerchantView;
import com.fasterxml.jackson.databind.JsonNode;
import okhttp3.HttpUrl;

import java.time.LocalTime;

/**
 * @author leibrother
 */
public class FarawayMerchantHandler implements CommandHandler {

    private final Requester request;

    public FarawayMerchantHandler(Bot bot) {
        this.request = Requester.getInstance();
    }

    @Override
    public void handle(MessageContext context, Command command) {
        if (LocalTime.now().isBefore(LocalTime.of(8, 0))) {
            context.reply(Message.text("远行商人已经休息了哦，请早上8点后再来吧~"));
            return;
        }
        JsonNode data = data();
        JsonNode rounds = data.get("rounds");
        int index = rounds.size() - 1;
        JsonNode round = rounds.get(index);
        FarawayMerchantView view = new FarawayMerchantView(round);
        context.reply(view.render());
    }

    private JsonNode data() {
        HttpUrl url = request.https("static.gamecenter.qq.com", "/game_tool/dynamic_backend_data/1110613799-faraway-merchant.json");
        return request.json().get(url);
    }

}
