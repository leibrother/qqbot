package cc.rapidev.qqbot.extension.push;

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

}
