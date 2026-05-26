package cc.rapidev.qqbot.extension;

import cc.rapidev.qqbot.Bot;

/**
 * @author leibrother
 */
public interface Extension {

    void ready(Bot bot);

    void destroy() throws Exception;

}
