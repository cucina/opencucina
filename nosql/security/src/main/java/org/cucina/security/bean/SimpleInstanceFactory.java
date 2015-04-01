package org.cucina.security.bean;

import org.apache.commons.lang3.ClassUtils;

import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 *
 * @author vlevine
  */
public class SimpleInstanceFactory
    implements InstanceFactory {
    private static final Logger LOG = LoggerFactory.getLogger(SimpleInstanceFactory.class);

    /**
     *
     *
     * @param <T> .
     * @param type .
     *
     * @return .
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T getBean(String type) {
        try {
            return (T) BeanUtils.instantiate(ClassUtils.getClass(type));
        } catch (BeanInstantiationException e) {
            LOG.error("Oops", e);
        } catch (ClassNotFoundException e) {
            LOG.error("Oops", e);
        }

        return null;
    }

    /**
     *
     *
     * @param <T> .
     * @param type .
     *
     * @return .
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> Class<T> getClassType(String type) {
        try {
            return (Class<T>) ClassUtils.getClass(type);
        } catch (ClassNotFoundException e) {
            LOG.error("Oops", e);
        }

        return null;
    }

    /**
     *
     *
     * @param className .
     * @param property .
     *
     * @return .
     */
    @Override
    public boolean isForeignKey(String className, String property) {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     *
     *
     * @param className .
     * @param property .
     *
     * @return .
     */
    @Override
    public String getPropertyType(String className, String property) {
        Class<?> clazz;

        try {
            clazz = ClassUtils.getClass(className);

            BeanWrapper bw = new BeanWrapperImpl(clazz);

            return bw.getPropertyType(property).getName();
        } catch (ClassNotFoundException e) {
            LOG.error("Oops", e);
        }

        return null;
    }
}
