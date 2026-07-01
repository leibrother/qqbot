package cc.rapidev.qqbot.message.member;

import cc.rapidev.qqbot.database.BotDatabase;
import cc.rapidev.qqbot.database.entity.Table;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author leibrother
 */
public class MemberRepository {

    private final BotDatabase database;
    private final Table table;

    public MemberRepository(BotDatabase database) {
        this.database = database;
        this.table = this.database.register(MemberEntity.class);
    }

    public List<MemberEntity> list(String topic) {
        return this.database.execute((handle) -> handle
                .select("SELECT * FROM %s WHERE topic = ?".formatted(table.name()), topic)
                .mapToBean(MemberEntity.class)
                .stream()
                .toList()
        );
    }

    public void store(MemberEntity entity) {
        if (exists(entity)) {
            this.update(entity);
        } else {
            this.insert(entity);
        }
    }

    private boolean exists(MemberEntity member) {
        Optional<Map<String, Object>> result = this.database.one("""
                SELECT COUNT(*) FROM %s WHERE id = ? AND topic = ?
                """.formatted(table.name()), member.getId(), member.getTopic());
        if (result.isPresent()) {
            int count = (int) result.get().values().stream().findFirst().orElse(0);
            return count > 0;
        }
        return false;
    }

    private void insert(MemberEntity member) {
        this.database.update("""
                INSERT INTO %s (id, topic, openid, avatar, username, bot) VALUES(:id, :topic, :openid, :avatar, :username, :bot)
                """.formatted(table.name()), member);
    }

    private void update(MemberEntity member) {
        this.database.update("""
                UPDATE %s SET openid = :openid, avatar = :avatar, username = :username, bot = :bot WHERE id = :id AND topic = :topic
                """.formatted(table.name()), member);
    }

}
