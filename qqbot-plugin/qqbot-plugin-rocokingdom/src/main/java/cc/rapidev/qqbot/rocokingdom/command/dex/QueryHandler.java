package cc.rapidev.qqbot.rocokingdom.command.dex;

import cc.rapidev.qqbot.api.model.Message;
import cc.rapidev.qqbot.common.utils.StringUtils;
import cc.rapidev.qqbot.extension.command.Command;
import cc.rapidev.qqbot.extension.command.CommandHandler;
import cc.rapidev.qqbot.message.MessageContext;
import cc.rapidev.qqbot.rocokingdom.repository.RocokingdomDex;
import cc.rapidev.qqbot.rocokingdom.service.RocokingdomDexService;
import cc.rapidev.qqbot.rocokingdom.view.RocokingdomDexListView;
import cc.rapidev.qqbot.rocokingdom.view.RocokingdomDexView;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author leibrother
 */
@Slf4j
public class QueryHandler implements CommandHandler {

    private final RocokingdomDexService service;

    public QueryHandler(RocokingdomDexService service) {
        this.service = service;
    }

    @Override
    public void handle(MessageContext context, Command command) {
        String name = command.content().trim();
        if (StringUtils.isEmpty(name)) {
            context.reply(Message.text("发送精灵名称进行查询"));
            return;
        }
        List<RocokingdomDex> list = service.findByFullnameLike(name);
        if (list.isEmpty()) {
            context.reply(Message.text("未找到相关的精灵"));
        } else if (list.size() > 1) {
            RocokingdomDexListView view = new RocokingdomDexListView(list);
            context.reply(view.render());
        } else {
            RocokingdomDexView view = new RocokingdomDexView(list.getFirst());
            context.reply(view.render());
        }
    }

}
