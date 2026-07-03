package cc.rapidev.qqbot.rocokingdom.command;

import cc.rapidev.qqbot.extension.command.CommandHandlerSet;
import cc.rapidev.qqbot.extension.command.Keyword;
import cc.rapidev.qqbot.rocokingdom.command.dex.QueryHandler;
import cc.rapidev.qqbot.rocokingdom.service.RocokingdomDexService;

/**
 * @author leibrother
 */
public class RocokingdomDexHandler extends CommandHandlerSet {

    private final RocokingdomDexService service;

    public RocokingdomDexHandler(RocokingdomDexService service) {
        this.service = service;
        super(new QueryHandler(service));
        add(new Keyword("同步", "同步数据"), (_, _) -> this.service.sync());
    }

}
