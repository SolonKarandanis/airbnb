package com.solon.airbnb.infrastructure.config.context;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class SpringContextConfig implements BeanFactoryAware{


	private static BeanFactory factory;

    /**
     * Returns the Spring managed bean instance of the given class type if it exists.
     * Returns null otherwise.
     *
     * @param beanClass
     * @return
     */
    public static <T extends Object> T getBean(Class<T> beanClass) {
        return factory.getBean(beanClass);
    }

    /**
     * Returns the Spring managed bean instance of the given name if it exists.
     * Returns null otherwise.
     *
     * @param beanName
     * @return
     */
    public static Object getBean(String beanName) {
        return factory.getBean(beanName);
    }

    /**
     * Returns the type of Spring managed bean of the given name if it exists.
     * Returns null otherwise.
     *
     * @param beanName
     * @return
     */
    public static Class<?> getType(String beanName) {
        return factory.getType(beanName);
    }

    /**
     * Returns the Spring managed bean instance of the given name and class type if it exists.
     * Returns null otherwise.
     *
     * @param beanClass
     * @return
     */
    public static <T extends Object> T getBean(String beanName, Class<T> beanClass) {
        return factory.getBean(beanName, beanClass);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        SpringContextConfig.factory = beanFactory;
    } 

}
