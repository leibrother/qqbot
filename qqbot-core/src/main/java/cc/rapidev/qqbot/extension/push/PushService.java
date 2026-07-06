package cc.rapidev.qqbot.extension.push;

import cc.rapidev.qqbot.api.model.Message;
import cc.rapidev.qqbot.api.model.MessageMedia;
import cc.rapidev.qqbot.common.Topic;

import java.util.List;

/**
 * @author leibrother
 */
public interface PushService {

    void open(Topic topic);

    void close(Topic topic);

    boolean isOpen(Topic topic);

    List<Topic> getOpenTopicList();

    void push(Topic topic, Message message);

    void push(Topic topic, MessageMedia media);

    void push(Message message);

    void push(MessageMedia media);

}
