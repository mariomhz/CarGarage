package com.cargarage.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple BeanFactory that creates and caches instances of classes based on a configuration property.
 * The class name of the implementation is specified in the application properties file under the key "repository.implementation".
 */

@Component
public class BeanFactory {

    private final Map<Class<?>, Object> beanCache = new HashMap<>();

    @Value("${repository.implementation}")
    private String repositoryClassName;

    public <T> T getBean(Class<T> interfaceType) {
        if (beanCache.containsKey(interfaceType)) {
            System.out.println("getting bean cache from: " + interfaceType.getName());
            return interfaceType.cast(beanCache.get(interfaceType));
        }

        System.out.println("creating new bean for: " + interfaceType.getName());

        try {
            Class<?> clazz = Class.forName(repositoryClassName);

            if (!interfaceType.isAssignableFrom(clazz)) {
                throw new RuntimeException(
                        "Class " + repositoryClassName + " does not implement " + interfaceType.getName()
                );
            }

            Object instance = clazz.getDeclaredConstructor().newInstance();

            beanCache.put(interfaceType, instance);

            System.out.println("Bean created successfully.");

            return interfaceType.cast(instance);

        } catch (ClassNotFoundException e) {
            throw new RuntimeException("class not found: " + repositoryClassName, e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("class does not have empty constructor: " + repositoryClassName, e);
        } catch (Exception e) {
            throw new RuntimeException("error: " + e);
        }
    }
}
