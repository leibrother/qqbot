package cc.rapidev.qqbot.message;

/**
 * 消息处理器
 * <p>本框架的核心接口，通过实现此接口可完成对所有事件的处理</p>
 *
 * @author leibrother
 */
@FunctionalInterface
public interface MessageHandler extends Comparable<MessageHandler> {

    default int order() {
        return (Integer.MAX_VALUE - 1) / 2;
    }

    default int compareTo(MessageHandler handler) {
        return Integer.compare(order(), handler.order());
    }

    /**
     * 是否必须执行
     *
     * @return 如果为true则表示其不受context的
     */
    default boolean isRequired() {
        return false;
    }

    void handle(MessageContext context);

}
