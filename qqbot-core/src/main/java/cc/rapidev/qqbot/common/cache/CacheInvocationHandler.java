package cc.rapidev.qqbot.common.cache;

import cc.rapidev.qqbot.common.annotations.Param;
import cc.rapidev.qqbot.common.utils.el.Evaluator;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author leibrother
 */
public class CacheInvocationHandler implements InvocationHandler {

    private final Object target;
    private final Map<Method, Method> methods = new HashMap<>();

    public CacheInvocationHandler(Object target) {
        this.target = target;
    }

    private Method findImplMethod(Method interfaceMethod) {
        return methods.computeIfAbsent(interfaceMethod, m -> {
            try {
                return target.getClass().getMethod(m.getName(), m.getParameterTypes());
            } catch (NoSuchMethodException e) {
                return m;
            }
        });
    }

    @Override
    public Object invoke(Object proxy, Method interfaceMethod, Object[] args) throws Throwable {
        Method method = findImplMethod(interfaceMethod);
        Cacheable cache = method.getAnnotation(Cacheable.class);
        CacheEvict evict = method.getAnnotation(CacheEvict.class);
        if (evict != null) {
            handleEvict(evict, method, args);
        }
        if (cache != null) {
            return handleCache(cache, method, args);
        }
        return method.invoke(target, args);
    }

    private Object handleCache(Cacheable cache, Method method, Object[] args) {
        String key = evalKey(cache.key(), method, args);
        CacheManager manager = CacheManager.getInstance();
        Object value = manager.get(key);
        if (value != null) {
            return value;
        }
        try {
            value = method.invoke(target, args);
            if (value != null) {
                manager.set(key, value, cache.ttl());
            }
            return value;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void handleEvict(CacheEvict evict, Method method, Object[] args) {
        String key = evalKey(evict.key(), method, args);
        CacheManager manager = CacheManager.getInstance();
        manager.remove(key);
    }

    private String evalKey(String expression, Method method, Object[] args) {
        Map<String, Object> context = new HashMap<>();
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            Param param = parameter.getAnnotation(Param.class);
            if (param != null) {
                context.put(param.value(), args[i]);
            } else {
                context.put(parameter.getName(), args[i]);
            }
        }
        Object result = Evaluator.eval(expression, context);
        if (result == null) {
            throw new IllegalArgumentException("EL expression evaluated to null: " + expression);
        } else if (!(result instanceof String)) {
            throw new IllegalArgumentException("EL expression must evaluate to String, but got: " + result.getClass() + " from: " + expression);
        } else {
            return (String) result;
        }
    }

}
