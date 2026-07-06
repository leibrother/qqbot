package cc.rapidev.qqbot.extension.push;

import cc.rapidev.qqbot.Bot;
import cc.rapidev.qqbot.api.model.Message;
import cc.rapidev.qqbot.api.model.MessageMedia;
import cc.rapidev.qqbot.api.response.MessageMediaResponse;
import cc.rapidev.qqbot.common.Topic;
import cc.rapidev.qqbot.common.annotations.Param;
import cc.rapidev.qqbot.common.cache.CacheEvict;
import cc.rapidev.qqbot.common.cache.Cacheable;
import cc.rapidev.qqbot.extension.push.repository.PushOpenEntity;
import cc.rapidev.qqbot.extension.push.repository.PushOpenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


/**
 * @author leibrother
 */
public class PushServiceCacheable implements PushService {

    private final Logger logger = LoggerFactory.getLogger(PushServiceCacheable.class);
    private final Bot bot;
    private final PushOpenRepository repository;

    public PushServiceCacheable(Bot bot) {
        this.bot = bot;
        this.repository = new PushOpenRepository(bot.database());
    }

    @CacheEvict(key = "'push_open:' + topic.code")
    @Override
    public void open(@Param("topic") Topic topic) {
        PushOpenEntity bean = new PushOpenEntity(topic.code());
        if (!this.repository.exists(bean)) {
            this.repository.insert(bean);
        }
    }

    @CacheEvict(key = "'push_open:' + topic.code")
    @Override
    public void close(@Param("topic") Topic topic) {
        PushOpenEntity bean = new PushOpenEntity(topic.code());
        if (this.repository.exists(bean)) {
            this.repository.delete(bean);
        }
    }

    @Cacheable(key = "'push_open:' + topic.code")
    @Override
    public boolean isOpen(@Param("topic") Topic topic) {
        PushOpenEntity bean = new PushOpenEntity(topic.code());
        return this.repository.exists(bean);
    }

    @Override
    public List<Topic> getOpenTopicList() {
        return this.repository.find()
                .stream()
                .map(PushOpenEntity::getTopic)
                .map(Topic::resolve)
                .toList();
    }

    @Override
    public void push(Topic topic, Message message) {
        try {
            bot.send(topic, message);
        } catch (Exception e) {
            logger.error("推送消息到{}失败", topic.code(), e);
        }
    }

    @Override
    public void push(Topic topic, MessageMedia media) {
        try {
            MessageMediaResponse response = bot.send(topic, media);
            if (!media.getSrvSendMsg()) {
                bot.send(topic, Message.media(response));
            }
        } catch (Exception e) {
            logger.error("推送消息到{}失败", topic.code(), e);
        }
    }

    @Override
    public void push(Message message) {
        List<Topic> topics = getOpenTopicList();
        for (Topic topic : topics) {
            push(topic, message);
        }
    }

    @Override
    public void push(MessageMedia media) {
        List<Topic> topics = getOpenTopicList();
        for (Topic topic : topics) {
            push(topic, media);
        }
    }

}
