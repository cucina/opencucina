package org.cucina.core.spring;

import org.springframework.expression.BeanResolver;
import org.springframework.expression.EvaluationContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Attempts to instantiate an object of the given type
 *
 */
public class ClassResolver
    implements BeanResolver {
    private static final Logger LOG = LoggerFactory.getLogger(ClassResolver.class);

    /**
     *
     *
     * @param path class name
     *
     * @return instance of the class or null
     */
    public Object resolve(EvaluationContext context, String path) {
        try {
            Class<?> clazz = Class.forName(path);

            return clazz.newInstance();
        } catch (ClassNotFoundException ex) {
            LOG.error("Oops", ex);
        } catch (InstantiationException ex) {
            LOG.error("Oops", ex);
        } catch (IllegalAccessException ex) {
            LOG.error("Oops", ex);
        }

        return null;
    }
}
