package cc.rapidev.qqbot.boot.plugin.command;

import cc.rapidev.qqbot.message.MessageContext;
import cc.rapidev.qqbot.message.matcher.MatcherMessageHandler;
import cc.rapidev.qqbot.message.matcher.MatcherService;

import java.util.List;

/**
 * @author leibrother
 */
public class PluginDisableCommand extends MatcherMessageHandler {

    @Override
    public List<String> keywords() {
        return List.of("禁用插件");
    }

    @Override
    public void process(MessageContext context, MatcherService matcher) {

    }

}
