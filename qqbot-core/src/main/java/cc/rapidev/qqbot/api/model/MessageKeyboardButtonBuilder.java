package cc.rapidev.qqbot.api.model;

import java.util.List;

/**
 * 消息按钮构建器
 *
 * @author leibrother
 */
public class MessageKeyboardButtonBuilder {

    private final int type;
    private String id;
    private String label;
    private String visitedLabel;
    private String data;
    private boolean reply;
    private boolean enter;
    private Integer anchor;
    private String unsupportedTips;
    private int permissionType;
    private List<String> specifyUserIds;
    private List<String> specifyRoleIds;

    public MessageKeyboardButtonBuilder(int type) {
        if (!List.of(0, 1, 2).contains(type)) {
            throw new RuntimeException("unsupported button type: " + type);
        }
        this.type = type;
        this.everyone();
    }

    public MessageKeyboardButton build() {
        MessageKeyboardButton.Render render = new MessageKeyboardButton.Render(label, visitedLabel);
        MessageKeyboardButton.Permission permission = new MessageKeyboardButton.Permission(permissionType, specifyUserIds, specifyRoleIds);
        MessageKeyboardButton.Action action = new MessageKeyboardButton.Action(type, data, reply, enter, anchor, unsupportedTips, permission);
        return new MessageKeyboardButton(id, render, action);
    }

    public MessageKeyboardButtonBuilder id(String id) {
        this.id = id;
        return this;
    }

    public MessageKeyboardButtonBuilder label(String label) {
        this.label = label;
        this.visitedLabel = label;
        return this;
    }

    public MessageKeyboardButtonBuilder label(String label, String visitedLabel) {
        this.label = label;
        this.visitedLabel = visitedLabel;
        return this;
    }

    public MessageKeyboardButtonBuilder data(String data) {
        this.data = data;
        return this;
    }

    public MessageKeyboardButtonBuilder reply(boolean reply) {
        this.reply = reply;
        return this;
    }

    public MessageKeyboardButtonBuilder enter() {
        this.enter = true;
        return this;
    }

    public MessageKeyboardButtonBuilder anchor(Integer anchor) {
        this.anchor = anchor;
        return this;
    }

    public MessageKeyboardButtonBuilder unsupportedTips(String unsupportedTips) {
        this.unsupportedTips = unsupportedTips;
        return this;
    }

    public MessageKeyboardButtonBuilder manager() {
        this.permissionType = 1;
        this.specifyUserIds = null;
        this.specifyRoleIds = null;
        return this;
    }

    public MessageKeyboardButtonBuilder everyone() {
        this.permissionType = 2;
        this.specifyUserIds = null;
        this.specifyRoleIds = null;
        return this;
    }

    public MessageKeyboardButtonBuilder specifyUserIds(List<String> ids) {
        this.permissionType = 0;
        this.specifyUserIds = ids;
        this.specifyRoleIds = null;
        return this;
    }

    public MessageKeyboardButtonBuilder specifyRoleIds(List<String> ids) {
        this.permissionType = 3;
        this.specifyUserIds = null;
        this.specifyRoleIds = ids;
        return this;
    }

}