package cc.rapidev.qqbot.api.model;

import cc.rapidev.qqbot.api.response.MessageMediaResponse;
import lombok.*;

/**
 * @author leibrother
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class GuildMessage {

    private String content;
    private String image;
    private MessageArk ark;
    private MessageMarkdown markdown;
    private String msgId;
    private String eventId;

    public static GuildMessage form(Message msg) {
        GuildMessage message = new GuildMessage();
        message.setMsgId(msg.getMsgId());
        message.setEventId(msg.getEventId());
        if (msg.isText()) {
            message.setContent(msg.getContent());
        } else if (msg.isArk()) {
            message.setArk(msg.getArk());
        } else if (msg.isMarkdown()) {
            message.setMarkdown(msg.getMarkdown());
        } else if (msg.isMedia()) {
            MessageMediaResponse media = msg.getMedia();
            if (media.isImage()) {
                message.setImage(media.getFileLink());
            } else {
                throw new IllegalArgumentException("频道/频道私信无法发送富媒体消息");
            }
        }
        return message;
    }

}
