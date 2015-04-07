package org.cucina.engine.client.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.messaging.MessageChannel;
import org.springframework.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
@ManagedResource
public class OperativeFactoryImpl
    implements OperativeFactory, ApplicationContextAware, InitializingBean {
    private static final Logger LOG = LoggerFactory.getLogger(OperativeFactoryImpl.class);
    private ApplicationContext applicationContext;
    private Map<String, Operative> channels = new HashMap<String, Operative>();
    private ObjectPool<Operative> queuePool;
    private String operativeBeanName = "operative";

    /**
    * JAVADOC Method Level Comments
    *
    * @param applicationContext JAVADOC.
    *
    * @throws BeansException JAVADOC.
    */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
        throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     *
     *
     * @return .
     */
    @ManagedAttribute
    public String[] getConversations() {
        return channels.keySet().toArray(new String[channels.size()]);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param operativeName JAVADOC.
     */
    public void setOperativeBeanName(String operativeBeanName) {
        this.operativeBeanName = operativeBeanName;
    }

    /**
     *
     *
     * @throws Exception .
     */
    @Override
    public void afterPropertiesSet()
        throws Exception {
        queuePool = new GenericObjectPool<Operative>(new QueuePoolFactory(applicationContext,
                    operativeBeanName));
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param conversationId JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public Operative createOperative(String conversationId) {
        Operative operative = obtain();

        Assert.notNull(operative, "Failed to get a channel");

        if (LOG.isDebugEnabled()) {
            LOG.debug("For conversationId '" + conversationId + "' adding operative " + operative);
        }

        channels.put(conversationId, operative);

        return operative;
    }

    /**
     * Used by integration router.
     *
     * @param conversationId .
     *
     * @return JAVADOC.
     */
    @Override
    public MessageChannel findChannel(String conversationId) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Request for conversationId '" + conversationId + "' " + channels);
        }

        Operative op = channels.get(conversationId);

        return (op == null) ? null : op.getReplyChannel();
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param operative JAVADOC.
     */
    @Override
    public void releaseConversation(String conversationId) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Releasing conversation " + conversationId);
        }

        try {
            queuePool.returnObject(channels.remove(conversationId));
        } catch (Exception e) {
            LOG.error("Oops", e);
        }
    }

    private Operative obtain() {
        try {
            return queuePool.borrowObject();
        } catch (Exception e) {
            LOG.error("Oops", e);
        }

        return null;
    }

    private static class QueuePoolFactory
        extends BasePooledObjectFactory<Operative> {
        private ApplicationContext applicationContext;
        private String operativeName = "operative";

        public QueuePoolFactory(ApplicationContext applicationContext, String operativeBeanName) {
            this.applicationContext = applicationContext;
            this.operativeName = operativeBeanName;
        }

        @Override
        public Operative create()
            throws Exception {
            OperativeImpl operative = applicationContext.getBean(operativeName, OperativeImpl.class);

            Assert.notNull(operative, "No bean " + operativeName + " found");

            operative.setReplyChannel(new QueueChannel(10));

            return operative;
        }

        @Override
        public PooledObject<Operative> wrap(Operative q) {
            return new DefaultPooledObject<Operative>(q);
        }
    }
}
