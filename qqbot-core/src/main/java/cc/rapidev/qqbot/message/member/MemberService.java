package cc.rapidev.qqbot.message.member;

import cc.rapidev.qqbot.Bot;
import cc.rapidev.qqbot.common.Topic;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author leibrother
 */
public class MemberService {

    private final MemberRepository repository;
    private final Map<String, Map<String, Member>> cache = new ConcurrentHashMap<>();

    public MemberService(Bot bot) {
        this.repository = new MemberRepository(bot.database());
    }

    private void refresh(String topic) {
        List<MemberEntity> records = this.repository.list(topic);
        Map<String, Member> map = new ConcurrentHashMap<>();
        records.stream()
                .map(MemberEntity::toRecord)
                .forEach((record) -> map.put(record.id(), record));
        this.cache.put(topic, map);
    }

    public Optional<Member> findById(Topic topic, String id) {
        List<Member> list = list(topic);
        Member member = list.stream().filter(record -> record.id().equals(id)).findFirst().orElse(null);
        return Optional.ofNullable(member);
    }

    public Optional<Member> findByOpenid(Topic topic, String openid) {
        List<Member> list = list(topic);
        Member member = list.stream().filter(record -> record.openid().equals(openid)).findFirst().orElse(null);
        return Optional.ofNullable(member);
    }

    public List<Member> list(Topic topic) {
        String code = topic.code();
        if (!this.cache.containsKey(code)) {
            this.refresh(code);
        }
        return this.cache.get(code).values().stream().toList();
    }

    public void store(Member member) {
        String id = member.id();
        String topic = member.topic().code();
        if (!this.cache.containsKey(topic)) {
            this.refresh(topic);
        }
        Member history = this.cache.get(topic).get(id);
        if (member.equals(history)) {
            return;
        }
        this.repository.store(MemberEntity.of(member));
        this.cache.get(topic).put(id, member);
    }

}
