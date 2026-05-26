package cc.rapidev.qqbot.extension.admin.repository;

import cc.rapidev.qqbot.database.BotDatabase;
import cc.rapidev.qqbot.database.repository.user.UserEntity;
import cc.rapidev.qqbot.message.model.Author;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author leibrother
 */
public class AdministratorRepository {

    private final BotDatabase database;

    public AdministratorRepository(BotDatabase database) {
        this.database = database;
    }

    private List<String> get() {
        Optional<Object> optional = this.database.parameters().get("bot.administrators");
        return optional.map(object -> List.of(object.toString().split(","))).orElse(Collections.emptyList());
    }

    private void set(List<String> ids) {
        String value = String.join(",", ids);
        this.database.parameters().set("bot.administrators", value);
    }

    public List<Author> administrators() {
        List<String> ids = this.get();
        if (ids.isEmpty()) {
            return Collections.emptyList();
        }
        return this.database.users()
                .findByOpenIds(ids)
                .stream()
                .map(UserEntity::toAuthor)
                .toList();
    }

    public void add(Author author) {
        List<String> ids = this.get();
        if (!ids.contains(author.openid())) {
            this.set(Stream.concat(ids.stream(), Stream.of(author.openid())).toList());
        }
    }

    public void remove(Author author) {
        List<String> ids = this.get().stream().filter(id -> !id.equals(author.openid())).toList();
        this.set(ids);
    }

}
