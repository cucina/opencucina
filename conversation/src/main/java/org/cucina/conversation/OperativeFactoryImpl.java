package org.cucina.conversation;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.messaging.MessageChannel;
import org.springframework.util.Assert;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
@ManagedResource
public class OperativeFactoryImpl
    implements OperativeFactory {
    private static final Logger LOG = LoggerFactory.getLogger(OperativeFactoryImpl.class);
    private Map<String, Operative> channels = new HashMap<String, Operative>();
    private ObjectPool<Operative> activeOperativePool;
    private ObjectPool<Operative> passiveOperativePool;

    /**
     * Creates a new OperativeFactoryImpl object.
     *
     * @param activeOperativePoolFactory .
     */
    public OperativeFactoryImpl(BasePooledObjectFactory<Operative> activeOperativePoolFactory,
        BasePooledObjectFactory<Operative> passiveOperativePoolFactory) {
        if (activeOperativePoolFactory != null) {
            activeOperativePool = new GenericObjectPool<Operative>(activeOperativePoolFactory);
        }

        if (passiveOperativePoolFactory != null) {
            passiveOperativePool = new GenericObjectPool<Operative>(passiveOperativePoolFactory);
        }
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
     * @param conversationId JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public Operative createOperative(String conversationId) {
        Assert.notNull(activeOperativePool,
            "Cannot create operative as there is no activeOperativePoolFactory");

        Operative operative;

        try {
            operative = activeOperativePool.borrowObject();
        } catch (Exception e) {
            LOG.error("Oops", e);
            throw new RuntimeException("Oops", e);
        }

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

        if (op == null) {
            op = createPassiveOperative(conversationId);
        }

        return op.getCallbackChannel();
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
            activeOperativePool.returnObject(channels.remove(conversationId));
        } catch (Exception e) {
            LOG.error("Oops", e);
        }
    }

    private Operative createPassiveOperative(String conversationId) {
        Assert.notNull(passiveOperativePool,
            "Cannot create operative as there is no passiveOperativePoolFactory");

        Operative operative;

        try {
            operative = passiveOperativePool.borrowObject();
        } catch (Exception e) {
            LOG.error("Oops", e);
            throw new RuntimeException("Oops", e);
        }

        Assert.notNull(operative, "Failed to get a channel");

        if (LOG.isDebugEnabled()) {
            LOG.debug("For conversationId '" + conversationId + "' adding operative " + operative);
        }

        if (StringUtils.isNotEmpty(conversationId)) {
            channels.put(conversationId, operative);
        }

        return operative;
    }
}
