package com.paimonx.mask.spi;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xu
 * @date 2022/3/17
 */
public class MaskServiceLoader {

    private static final Map<Class<?>, Collection<Object>> SERVICES = new ConcurrentHashMap<>();

    /**
     * Register service.
     *
     * @param serviceInterface service interface
     */
    public static void register(final Class<?> serviceInterface) {
        if (!SERVICES.containsKey(serviceInterface)) {
            SERVICES.put(serviceInterface, load(serviceInterface));
        }
    }

    private static <T> Collection<Object> load(final Class<T> serviceInterface) {
        Collection<Object> result = new LinkedList<>();
        for (T each : ServiceLoader.load(serviceInterface)) {
            result.add(each);
        }
        return result;
    }

    /**
     * Get singleton service instances.
     *
     * @param service service class
     * @param <T>     type of service
     * @return service instances
     */
    @SuppressWarnings("unchecked")
    public static <T> Collection<T> getSingletonServiceInstances(final Class<T> service) {
        return (Collection<T>) SERVICES.getOrDefault(service, Collections.emptyList());
    }

    /**
     * New service instances.
     *
     * @param service service class
     * @param <T>     type of service
     * @return service instances
     */
    @SuppressWarnings("unchecked")
    public static <T> Collection<T> newServiceInstances(final Class<T> service) {
        if (!SERVICES.containsKey(service)) {
            return Collections.emptyList();
        }
        Collection<Object> services = SERVICES.get(service);
        if (services.isEmpty()) {
            return Collections.emptyList();
        }
        Collection<T> result = new ArrayList<>(services.size());
        for (Object each : services) {
            result.add((T) newServiceInstance(each.getClass()));
        }
        return result;
    }

    private static Object newServiceInstance(final Class<?> clazz) {
        try {
            return clazz.newInstance();
        } catch (final InstantiationException | IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
    }
}
