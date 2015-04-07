package org.cucina.engine;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.domain.Persistable;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import org.cucina.core.InstanceFactory;
import org.cucina.core.spring.SingletonBeanFactory;

import org.cucina.engine.definition.ProcessDefinition;
import org.cucina.engine.definition.Token;
import org.cucina.engine.model.ProcessToken;
import org.cucina.engine.repository.TokenRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * JAVADOC for Class Level
 *
 * @author vlevine
 */
@Component
public class TokenFactoryImpl
    implements TokenFactory, InitializingBean, ApplicationContextAware {
    private static final Logger LOG = LoggerFactory.getLogger(TokenFactoryImpl.class);
    private ApplicationContext applicationContext;
    private Class<?> tokenClass;
    private InstanceFactory instanceFactory;
    private TokenRepository tokenRepository;

    /**
     * Creates a new TokenFactoryImpl object.
     *
     * @param instanceFactory
     *            JAVADOC.
     * @param searchDao
     *            JAVADOC.
     */
    @Autowired
    public TokenFactoryImpl(InstanceFactory instanceFactory, TokenRepository tokenRepository) {
        this.instanceFactory = instanceFactory;
        this.tokenRepository = tokenRepository;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param arg0
     *            JAVADOC.
     *
     * @throws BeansException
     *             JAVADOC.
     */
    @Override
    public void setApplicationContext(ApplicationContext arg0)
        throws BeansException {
        this.applicationContext = arg0;
    }

    /**
     * Optional class of the token. By default, if not set or null, it is @see
     * {@link ProcessToken}
     *
     * @param tokenClass
     *            Class of the token to generate.
     */
    public void setTokenClass(Class<?> tokenClass) {
        this.tokenClass = tokenClass;
    }

    /**
     * Wire in default instanceFactory and seachDao. Sets default tokenClass if
     * null.
     *
     * @throws Exception
     *             JAVADOC.
     */
    @Override
    public void afterPropertiesSet()
        throws Exception {
        if (instanceFactory == null) {
            if (applicationContext.containsBean(SingletonBeanFactory.INSTANCE_FACTORY_ID)) {
                instanceFactory = (InstanceFactory) applicationContext.getBean(SingletonBeanFactory.INSTANCE_FACTORY_ID);
            } else {
                throw new IllegalArgumentException(
                    "The instanceFactory is null and not available in Spring context");
            }
        }

        if (tokenRepository == null) {
            if (applicationContext.containsBean("tokenRepository")) {
                tokenRepository = (TokenRepository) applicationContext.getBean("tokenRepository");
            } else {
                throw new IllegalArgumentException(
                    "The tokenRepository is null and not available in Spring context");
            }
        }

        if (tokenClass == null) {
            tokenClass = ProcessToken.class;
        }
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param definition
     *            JAVADOC.
     * @param domainObject
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public Token createToken(ProcessDefinition definition, Object domainObject) {
        Assert.notNull(definition, "The 'definition' parameter cannot be null.");
        Assert.notNull(domainObject, "The 'domainObject' parameter cannot be null.");
        Assert.isInstanceOf(Persistable.class, domainObject);

        Persistable<?> entity = (Persistable<?>) domainObject;

        if (entity.getId() != null) {
            // call to tokenRepository to find an existing one for the object
            Token token = tokenRepository.findByDomain(entity);

            if (token != null) {
                LOG.debug("Found existing token for the object :" + token);

                return token;
            }
        }

        Token token = instanceFactory.getBean(tokenClass.getSimpleName());

        token.setDomainObject(entity);
        token.setProcessDefinitionId(definition.getId());
        token.setPlaceId(definition.getStartState().getId());

        return token;
    }
}
