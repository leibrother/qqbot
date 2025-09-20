package cc.rapidev.qqbot.message;

/**
 * @author leibrother
 */
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
    default boolean must() {
        return false;
    }

    void handle(MessageContext context);

}
