package cc.rapidev.qqbot.api.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @author leibrother
 */
public record MessageKeyboard(
        /* 模板ID */
        String id,
        /* 自定义按钮内容 */
        MessageKeyboardContent content
) {

    /* 消息按钮最大矩阵 */
    private static final int MAX_SIZE = 5;

    public record MessageKeyboardContent(
            /* 按钮组行 */
            List<MessageKeyboardButtons> rows
    ) {

        public MessageKeyboardContent() {
            this(new ArrayList<>(MAX_SIZE));
        }

    }

    public record MessageKeyboardButtons(
            /* 按钮组列 */
            List<MessageKeyboardButton> buttons
    ) {

        public MessageKeyboardButtons() {
            this(new ArrayList<>(MAX_SIZE));
        }

    }

    public MessageKeyboard() {
        this(null, new MessageKeyboardContent());
    }

    public MessageKeyboard(String id) {
        this(id, null);
    }

    /**
     * 添加一个按钮
     *
     * @param button 消息按钮
     */
    public void add(MessageKeyboardButton button) {
        this.add(0, button);
    }

    /**
     * 添加一个按钮
     *
     * @param row    添加到哪一行, max=5, 如果小于等于0则自动寻找
     * @param button 消息按钮
     */
    public void add(int row, MessageKeyboardButton button) {
        if (this.id != null && this.content == null) {
            throw new RuntimeException("cannot add buttons when using a keyboard template");
        }
        if (row > MAX_SIZE) {
            throw new RuntimeException("cannot set more than %d rows of buttons".formatted(MAX_SIZE));
        } else if (row <= 0) {
            row = IntStream.range(1, MAX_SIZE)
                    .filter(i -> {
                        if (this.content.rows.size() >= i) {
                            return this.content.rows.get(i - 1).buttons.size() < MAX_SIZE;
                        }
                        return true;
                    })
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("no available space to add more buttons"));
        }
        if (this.content.rows.size() < row) {
            this.content.rows.add(new MessageKeyboardButtons());
        }
        MessageKeyboardButtons rowButtons = this.content.rows.get(row - 1);
        if (rowButtons.buttons.size() >= MAX_SIZE) {
            throw new RuntimeException("row %d has no available space to add more buttons".formatted(row));
        }
        rowButtons.buttons.add(button);
    }

}
