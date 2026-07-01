package cc.rapidev.qqbot.extension.admin;

import cc.rapidev.qqbot.Bot;
import cc.rapidev.qqbot.common.Author;
import cc.rapidev.qqbot.common.utils.IdentityUtils;
import cc.rapidev.qqbot.message.MessageContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author leibrother
 */
public class AdminService {

    private final static String parameterKey = "bot.administrators";
    private final Logger logger = LoggerFactory.getLogger(AdminService.class);
    private final Bot bot;
    private final String passwd;

    protected AdminService(Bot bot) {
        this.bot = bot;
        this.passwd = IdentityUtils.shortID().toLowerCase().substring(0, 8);
        logger.info("administrator passwd: {}", passwd);
    }

    public List<String> administrators() {
        Optional<Object> optional = this.bot.parameters().get(parameterKey);
        if (optional.isPresent()) {
            String[] arr = optional.get().toString().split(",");
            return List.of(arr);
        }
        return List.of();
    }

    private void addAdministrator(Author author) {
        ArrayList<String> list = new ArrayList<>(administrators());
        list.add(author.openid());
        String ids = String.join(",", list);
        this.bot.parameters().set(parameterKey, ids);
        logger.info("add administrator: {}", author.openid());
    }


    public boolean become(Author author, String passwd) {
        if (isAdmin(author)) {
            return true;
        }
        if (this.passwd.equals(passwd) && author != null) {
            this.addAdministrator(author);
            return true;
        }
        return false;
    }

    public boolean isAdmin(Author author) {
        if (author != null) {
            return this.administrators().contains(author.openid());
        }
        return false;
    }

    public boolean isAdmin(MessageContext context) {
        Author author = context.author();
        return isAdmin(author);
    }

}
