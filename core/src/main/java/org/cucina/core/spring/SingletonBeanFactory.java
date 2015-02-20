package org.cucina.core.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.util.Assert;


/**
 * JAVADOC.
 *
 * @author $author$
 * @version $Revision$
  */
public class SingletonBeanFactory
    implements BeanFactory, BeanFactoryAware {
    /*
     * common bean names
     */

    /** persistenceService. */
    public static final String PERSISTENCE_SERVICE_ID = "persistenceService";

    /** contextService. */
    public static final String CONTEXT_SERVICE_ID = "contextService";

    /** instanceFactory */
    public static final String INSTANCE_FACTORY_ID = "instanceFactory";

    /** i18nService */
    public static final String I18N_SERVICE_ID = "i18nService";

    /** messageRepository */
    public static final String MESSAGE_REPOSITORY_ID = "messageRepository";

    /**
     * Singleton instance.
     */
    private static BeanFactory instance = new SingletonBeanFactory();
    private BeanFactory beanFactory;

    /**
     * Private constructor - we really want this to be a singleton.  That
     * way it can be used anywhere, even by code that has no idea about Spring.
     */
    private SingletonBeanFactory() {
    }

    /**
     * Simple delegate method to BeanFactory.
     * @param name the identifier of the prototype bean required.
     * @return array of aliases if there is a bean identified by name.
     * @throws BeansException if the bean does not exist.
     * @throws NullPointerException if the bean factory has not bean set.
     */
    public String[] getAliases(String name) {
        return getBeanFactory().getAliases(name);
    }

    /**
     * Simple delegate method to pull a bean out of the BeanFactory.  Intended
     * for use with prototype beans, but could actually be used to
     * locate non-prototypes as well.
     * @param name the identifier of the prototype bean required.
     * @return the bean identified by name.
     * @throws BeansException if the bean does not exist.
     * @throws NullPointerException if the bean factory has not bean set.
     */
    public Object getBean(String name) {
        return getBeanFactory().getBean(name);
    }

    /**
    * Simple delegate method to pull a bean out of the BeanFactory.
    * @param name the identifier of the prototype bean required.
    * @return the bean identified by name.
    * @throws BeansException if the bean does not exist.
    * @throws NullPointerException if the bean factory has not bean set.
    */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Object getBean(String name, Class type) {
        return getBeanFactory().getBean(name, type);
    }

    /**
     * Simple delegate method to pull a bean out of the BeanFactory.
     *
         * @param name the name of the bean to retrieve
         * @param args arguments to use if creating a prototype using explicit arguments to a
         * static factory method. It is invalid to use a non-null args value in any other case.
         * @return an instance of the bean
     *
     * @throws BeansException JAVADOC.
     */
    public Object getBean(String name, Object... args)
        throws BeansException {
        return getBeanFactory().getBean(name, args);
    }

    /**
     * Simple delegate method to pull a bean out of the BeanFactory.
     * @param requiredType the type of the bean that is required.
     *
     * @return the bean identified by requiredType.
     * @throws BeansException if the bean does not exist.
     */
    public <T> T getBean(Class<T> requiredType)
        throws BeansException {
        return getBeanFactory().getBean(requiredType);
    }

    @Override
    public <T> T getBean(Class<T> requiredType, Object... args)
        throws BeansException {
        return getBeanFactory().getBean(requiredType, args);
    }

    /**
     * Injected.
     * @param beanFactory
     */
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    /**
     * Singleton accessor for the one and only factory.  Not a classic singleton
     * accessor because it returns an interface.  The instance returned from this
     * factory method <em>is</em> a SingletonBeanFactory, but clients don't need
     * to know that unless they are testing, and need to explicitly call the
     * setter methods on the instance.
     * @return a delegate to the injected bean factory.
     */
    public static BeanFactory getInstance() {
        return instance;
    }

    /**
     * Simple delegate method to BeanFactory.
     *
     * @param name the identifier bean.
     *
     * @return true if the bean identified is a prototype bean.
     *
     * @throws NoSuchBeanDefinitionException JAVADOC.
     */
    public boolean isPrototype(String name)
        throws NoSuchBeanDefinitionException {
        return getBeanFactory().isPrototype(name);
    }

    /**
     * Simple delegate method to BeanFactory.
     * @param name the identifier of the prototype bean required.
     * @return true if there is a bean identified by name.
     * @throws BeansException if the bean does not exist.
     * @throws NullPointerException if the bean factory has not bean set.
     */
    public boolean isSingleton(String name) {
        return getBeanFactory().isSingleton(name);
    }

    /**
     * Simple delegate method to BeanFactory.
     * @param name the identifier of the prototype bean required.
     * @return type of bean if there is a bean identified by name.
     * @throws BeansException if the bean does not exist.
     * @throws NullPointerException if the bean factory has not bean set.
     */
    public Class<?> getType(String name) {
        return getBeanFactory().getType(name);
    }

    /**
     * Simple delegate method to BeanFactory.
     *
     * @param name the identifier bean.
     * @param targetType Class of the named bean.
     *
     * @return JAVADOC.
     *
     * @throws NoSuchBeanDefinitionException JAVADOC.
     */
    public boolean isTypeMatch(String name, @SuppressWarnings("rawtypes")
    Class targetType)
        throws NoSuchBeanDefinitionException {
        return getBeanFactory().isTypeMatch(name, targetType);
    }

    /**
     * Simple delegate method to BeanFactory.
     * @param name the identifier of the prototype bean required.
     * @return true if there is a bean identified by name.
     * @throws BeansException if the bean does not exist.
     * @throws NullPointerException if the bean factory has not bean set.
     */
    public boolean containsBean(String name) {
        return getBeanFactory().containsBean(name);
    }

    private BeanFactory getBeanFactory() {
        Assert.notNull(beanFactory, "BeanFactory is null, probably no Spring context");

        return beanFactory;
    }
}
