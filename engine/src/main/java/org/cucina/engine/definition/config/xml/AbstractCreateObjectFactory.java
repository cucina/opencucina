package org.cucina.engine.definition.config.xml;

import java.util.Map;

import org.apache.commons.digester3.Digester;
import org.apache.commons.digester3.ObjectCreationFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.Assert;
import org.xml.sax.Attributes;


/**
 * Sets created object instance on the item at the top of stack according the the 'name'
 * attribute.
 *
 * @author $Author: $
 * @version $Revision: $
  *
 * @param <T> class instance that will be created.
 */
public abstract class AbstractCreateObjectFactory<T>
    implements ObjectCreationFactory<T> {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractCreateObjectFactory.class);
    private Digester digester;

    /**
     * Set digester
     *
     * @param digester Digester.
     */
    @Override
    public void setDigester(Digester digester) {
        this.digester = digester;
    }

    /**
     * Get digester
     *
     * @return digester Digester.
     */
    @Override
    public Digester getDigester() {
        return digester;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param attributes JAVADOC.
     *
     * @return JAVADOC.
     *
     * @throws Exception JAVADOC.
     */
    @SuppressWarnings("unchecked")
    @Override
    public T createObject(Attributes attributes)
        throws Exception {
        T newObject = createObjectImpl(attributes);
        String propertyName = attributes.getValue("name");

        Assert.notNull(propertyName, "Element must have a non-empty attribute 'name'");

        Object subject = digester.peek();

        if (subject == null) {
            LOG.debug("Empty top object");

            return newObject;
        }

        BeanWrapper bw = new BeanWrapperImpl(subject);

        if (bw.isWritableProperty(propertyName)) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Setting '" + propertyName + "' to '" + newObject + "' on " + subject);
            }

            bw.setPropertyValue(propertyName, newObject);
        } else if (subject instanceof Map<?, ?>) {
            ((Map<String, T>) subject).put(propertyName, newObject);
        } else {
            throw new IllegalArgumentException("Property name [" + propertyName +
                "] does not exist on [" + subject.getClass().getSimpleName() + "]");
        }

        return newObject;
    }

    /**
     * For subclasses to override in order to generate object to set on stack top item.
     *
     * @return T.
     */
    protected abstract T createObjectImpl(Attributes attributes);
}
