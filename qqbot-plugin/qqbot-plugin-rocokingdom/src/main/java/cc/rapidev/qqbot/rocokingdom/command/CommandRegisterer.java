package cc.rapidev.qqbot.rocokingdom.command;

import cc.rapidev.qqbot.Bot;
import cc.rapidev.qqbot.extension.command.CommandHandlerSet;
import cc.rapidev.qqbot.extension.command.Keyword;
import cc.rapidev.qqbot.extension.command.KeywordRegisterer;
import cc.rapidev.qqbot.rocokingdom.service.RocokingdomDexService;

/**
 * @author leibrother
 */
public class CommandRegisterer implements KeywordRegisterer {

    private final Bot bot;

    public CommandRegisterer(Bot bot) {
        this.bot = bot;
    }

    @Override
    public void register(CommandHandlerSet entry) {
        RocokingdomDexService dexService = new RocokingdomDexService(bot);
        RocokingdomDexHandler dexHandler = new RocokingdomDexHandler(dexService);
        CommandHandlerSet group = new CommandHandlerSet(dexHandler);
        entry.add(new Keyword("洛克", "洛克王国世界"), group);
        group.add(new Keyword("精灵图鉴", "查询精灵详细信息"), dexHandler);
        group.add(new Keyword("远行商人", "查询远行商人正在出售的物品"), new FarawayMerchantHandler());
    }

}
