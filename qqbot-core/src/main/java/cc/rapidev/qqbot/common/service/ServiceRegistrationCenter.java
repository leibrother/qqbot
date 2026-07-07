package cc.rapidev.qqbot.common.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.SequencedMap;

/**
 * 服务注册器
 *
 * @author leibrother
 */
public class ServiceRegistrationCenter {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final SequencedMap<Class<?>, Object> services = new LinkedHashMap<>();

    public void add(Object service) {
        Class<?> serviceClass = service.getClass();
        if (services.containsKey(serviceClass) && !service.equals(services.get(serviceClass))) {
            logger.debug("service {} will be replaced by a new instance", serviceClass.getName());
        }
        services.put(serviceClass, service);
    }

    /**
     * 获取服务
     * <p>返回按照添加顺序倒序的第一个可分配的服务，如：</p>
     * <pre>
     *     // 现有两个服务ServiceA和继承ServiceA的ServiceB
     *     class ServiceA {
     *
     *     }
     *
     *     class ServiceB extends ServiceA {
     *
     *     }
     *
     *     // 先注入ServiceA，再注入ServiceB
     *     this.add(new ServiceA())
     *     this.add(new ServiceB())
     *     // 此时获取服务
     *     this.get(ServiceA.class) // 返回了ServiceB
     *     this.get(ServiceB.class) // 还是返回ServiceB
     *
     * </pre>
     *
     * @param clazz 服务类
     * @return Optional
     */
    public <T> Optional<T> get(Class<T> clazz) {
        SequencedMap<Class<?>, Object> map = this.services.reversed();
        for (Class<?> key : map.keySet()) {
            if (clazz.isAssignableFrom(key)) {
                Object service = map.get(key);
                return Optional.of(clazz.cast(service));
            }
        }
        return Optional.empty();
    }

    public <T> T use(Class<T> clazz) {
        Optional<T> optional = this.get(clazz);
        return optional.orElseThrow(() -> new IllegalStateException("service " + clazz.getName() + " is not registered!"));
    }

}
