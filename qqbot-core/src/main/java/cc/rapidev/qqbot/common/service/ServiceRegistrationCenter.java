package cc.rapidev.qqbot.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 服务注册器
 *
 * @author leibrother
 */
public class ServiceRegistrationCenter {

    private final Map<Class<?>, Object> services = new HashMap<>();

    public void add(Object service) {
        Class<?> clazz = service.getClass();
        if (services.containsKey(clazz)) {
            throw new IllegalStateException("service " + clazz.getName() + " is already registered!");
        }
        services.put(clazz, service);
    }

    public <T> Optional<T> get(Class<T> clazz) {
        Object service = services.get(clazz);
        if (service != null) {
            return Optional.of(clazz.cast(service));
        } else {
            List<Class<?>> candidate = services.keySet()
                    .stream()
                    .filter(clazz::isAssignableFrom)
                    .toList();
            if (candidate.isEmpty()) {
                return Optional.empty();
            } else if (candidate.size() == 1) {
                service = services.get(candidate.getFirst());
                return Optional.of(clazz.cast(service));
            } else {
                throw new IllegalArgumentException("found multiple services, please specify a more specific class");
            }
        }
    }

    public <T> T use(Class<T> clazz) {
        Optional<T> optional = this.get(clazz);
        return optional.orElseThrow(() -> new IllegalStateException("service " + clazz.getName() + " is not registered!"));
    }

}
