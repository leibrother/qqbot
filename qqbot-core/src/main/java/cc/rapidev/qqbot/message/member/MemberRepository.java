package cc.rapidev.qqbot.message.member;

import cc.rapidev.qqbot.database.BotDatabase;
import cc.rapidev.qqbot.database.repository.SimpleRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author leibrother
 */
public class MemberRepository extends SimpleRepository<MemberEntity> {

    public MemberRepository(BotDatabase database) {
        super(database, MemberEntity.class);
    }

    public List<MemberEntity> list(String topic) {
        Map<String, Object> map = new HashMap<>();
        map.put("topic", topic);
        return find(map);
    }

}
