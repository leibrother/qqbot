package cc.rapidev.qqbot.extension.admin;

import cc.rapidev.qqbot.common.utils.IdentityUtils;
import cc.rapidev.qqbot.extension.admin.repository.AdministratorRepository;
import cc.rapidev.qqbot.message.MessageContext;
import cc.rapidev.qqbot.message.model.Author;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author leibrother
 */
public class AdministratorService {

    private final Logger logger = LoggerFactory.getLogger(AdministratorService.class);
    private final AdministratorRepository repository;
    private final String passwd;
    private List<Author> administrators;

    protected AdministratorService(AdministratorRepository repository) {
        this.repository = repository;
        this.administrators = repository.administrators();
        this.passwd = IdentityUtils.shortID().toLowerCase().substring(0, 8);
        logger.info("administrator passwd: {}", passwd);
    }

    public boolean become(Author author, String passwd) {
        if (isAdmin(author)) {
            return true;
        }
        if (this.passwd.equals(passwd) && author != null) {
            this.repository.add(author);
            this.administrators = repository.administrators();
            logger.info("administrators add: {}", author.openid());
            return true;
        }
        return false;
    }

    public boolean isAdmin(Author author) {
        if (author != null) {
            return administrators.contains(author);
        }
        return false;
    }

    public boolean isAdmin(MessageContext context) {
        Author author = context.author();
        return isAdmin(author);
    }

}
