package cc.rapidev.qqbot.extension.settings.manager;

import java.util.Objects;

/**
 * @author leibrother
 */
public record Manager(String openid, boolean supermanager) {

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Manager manager = (Manager) o;
        return Objects.equals(openid, manager.openid);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(openid);
    }

}
