package cc.rapidev.qqbot.rocokingdom.command;

import cc.rapidev.qqbot.Bot;
import cc.rapidev.qqbot.common.request.Requester;
import cc.rapidev.qqbot.extension.command.Command;
import cc.rapidev.qqbot.extension.command.CommandHandler;
import cc.rapidev.qqbot.message.MessageContext;
import cc.rapidev.qqbot.rocokingdom.view.FarawayMerchantView;
import com.fasterxml.jackson.databind.JsonNode;
import okhttp3.HttpUrl;

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
