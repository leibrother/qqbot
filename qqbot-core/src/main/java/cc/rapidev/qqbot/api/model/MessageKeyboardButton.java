package cc.rapidev.qqbot.api.model;

/**
 * 消息按钮
 *
 * @author leibrother
 */
public record MessageKeyboardButton(String id, Render renderData, Action action) {

    public record Render(String label, String visitedLabel) {
    }

    public record Action(int type, String data, boolean reply, boolean enter, Integer anchor, String unsupportTips) {
    }

    /**
     * 创建跳转按钮构建器
     *
     * @return 消息按钮构建器
     */
    public static MessageKeyboardButtonBuilder jump() {
        return new MessageKeyboardButtonBuilder(0);
    }

    /**
     * 创建指令按钮构建器
     *
     * @return 消息按钮构建器
     */
    public static MessageKeyboardButtonBuilder command() {
        return new MessageKeyboardButtonBuilder(2);
    }

    /**
     * 创建回调按钮构建器
     *
     * @return 消息按钮构建器
     */
    public static MessageKeyboardButtonBuilder callback() {
        return new MessageKeyboardButtonBuilder(1);
    }

}
