package cc.rapidev.qqbot.message;

/**
 * 消息处理器注入器
 * <p>用于自动注入消息处理器，实现模块化与插件开发的重要接口</p>
 *
 * @author leibrother
 */
public interface MessageHandlerInjector {

    void inject(MessageDispatcher dispatcher);

}
