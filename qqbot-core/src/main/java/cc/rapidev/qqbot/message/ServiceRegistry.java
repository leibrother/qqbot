package cc.rapidev.qqbot.message;

import cc.rapidev.qqbot.common.utils.Asserts;
import cc.rapidev.qqbot.common.utils.CastUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 服务注册器
 *
 * @author leibrother
 */
public class ServiceRegistry {

    private final Map<String, Object> services = new HashMap<>();

    public void register(Object service) {
        Asserts.notnull(service, "service must not be null");
        services.put(service.getClass().getName(), service);
    }

    public void register(String name, Object service) {
        Asserts.notempty(name, "name must not be empty");
        Asserts.notnull(service, "service must not be null");
        if (this.services.containsKey(name)) {
            throw new IllegalArgumentException("service %s already exists".formatted(name));
        }
        this.services.put(name, service);
    }

    public Optional<Object> get(String name) {
        Asserts.notempty(name, "name must not be empty");
        return Optional.ofNullable(this.services.get(name));
    }

    public <T> Optional<T> get(Class<T> type) {
        Asserts.notnull(type, "type must not be null");
        List<Object> list = services.values()
                .stream()
                .filter(service -> type.isAssignableFrom(service.getClass()))
                .toList();
        if (list.isEmpty()) {
            return Optional.empty();
        } else if (list.size() == 1) {
            return Optional.of(CastUtils.cast(list.getFirst()));
        } else {
            throw new IllegalArgumentException("Found multiple services, please specify service name");
        }
    }

    public <T> Optional<T> get(String name, Class<T> type) {
        Asserts.notempty(name, "name must not be empty");
        Asserts.notnull(type, "type must not be null");
        Object service = get(name);
        if (service != null) {
            if (type.isAssignableFrom(service.getClass())) {
                return Optional.of(CastUtils.cast(service));
            } else {
                throw new IllegalArgumentException("service %s cannot be assigned as %s".formatted(name, type.getName()));
            }
        }
        return Optional.empty();
    }

}
