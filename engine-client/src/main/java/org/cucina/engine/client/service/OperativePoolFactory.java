package org.cucina.engine.client.service;

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
import org.springframework.messaging.PollableChannel;
import org.springframework.util.Assert;


/**
 *
 *
 * @author vlevine
  */
public class OperativePoolFactory
    extends BasePooledObjectFactory<Operative>
    implements ApplicationContextAware, InitializingBean {
    private ApplicationContext applicationContext;
    private ObjectPool<PollableChannel> mcPool;
    private String operativeName;

    /**
     * Creates a new OperativePoolFactory object.
     *
     * @param applicationContext .
     * @param operativeBeanName .
     */
    public OperativePoolFactory(String operativeBeanName) {
        this.operativeName = operativeBeanName;
    }

    /**
     *
     *
     * @param applicationContext .
     *
     * @throws BeansException .
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
        throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     *
     *
     * @param p .
     *
     * @throws Exception .
     */
    @Override
    public void activateObject(PooledObject<Operative> p)
        throws Exception {
        Operative operative = p.getObject();

        operative.setCallbackChannel(mcPool.borrowObject());
    }

    /**
     *
     *
     * @throws Exception .
     */
    @Override
    public void afterPropertiesSet()
        throws Exception {
        mcPool = new GenericObjectPool<PollableChannel>(new MessageChannelPoolFactory());
    }

    /**
     *
     *
     * @return .
     *
     * @throws Exception .
     */
    @Override
    public Operative create()
        throws Exception {
        Operative operative = applicationContext.getBean(operativeName, Operative.class);

        Assert.notNull(operative, "No bean " + operativeName + " found");

        return operative;
    }

    /**
     *
     *
     * @param p .
     *
     * @throws Exception .
     */
    @Override
    public void passivateObject(PooledObject<Operative> p)
        throws Exception {
        Operative op = p.getObject();

        mcPool.returnObject(op.getCallbackChannel());
    }

    /**
     *
     *
     * @param q .
     *
     * @return .
     */
    @Override
    public PooledObject<Operative> wrap(Operative q) {
        return new DefaultPooledObject<Operative>(q);
    }

    private static class MessageChannelPoolFactory
        extends BasePooledObjectFactory<PollableChannel> {
        @Override
        public PollableChannel create()
            throws Exception {
            return new QueueChannel(10);
        }

        @Override
        public PooledObject<PollableChannel> wrap(PollableChannel obj) {
            return new DefaultPooledObject<PollableChannel>(obj);
        }
    }
}
