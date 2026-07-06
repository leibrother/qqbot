package cc.rapidev.qqbot.rocokingdom.command;

import cc.rapidev.qqbot.api.model.Message;
import cc.rapidev.qqbot.extension.command.Command;
import cc.rapidev.qqbot.extension.command.CommandHandler;
import cc.rapidev.qqbot.message.MessageContext;
import cc.rapidev.qqbot.rocokingdom.model.Merchant;
import cc.rapidev.qqbot.rocokingdom.service.MerchantService;
import cc.rapidev.qqbot.rocokingdom.view.MerchantView;

import java.time.LocalTime;
import java.util.Optional;

/**
 * @author leibrother
 */
public class MerchantHandler implements CommandHandler {

    private final MerchantService service;

    public MerchantHandler(MerchantService service) {
        this.service = service;
    }

    @Override
    public void handle(MessageContext context, Command command) {
        LocalTime now = LocalTime.now();
        if (now.isBefore(LocalTime.of(MerchantService.START_HOUR, 0))) {
            context.reply(Message.text("远行商人已经休息了哦，请早上%s点后再来吧~".formatted(MerchantService.START_HOUR)));
            return;
        }
        Optional<Merchant> optional = service.nowadaysMerchant();
        if (optional.isPresent()) {
            MerchantView view = new MerchantView(optional.get());
            context.reply(view.render());
        } else {
            context.reply(Message.text("远行商人数据未更新，请稍等片刻"));
        }
    }

}
