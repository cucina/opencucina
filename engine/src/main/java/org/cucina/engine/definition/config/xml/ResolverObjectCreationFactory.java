
package org.cucina.engine.definition.config.xml;

import org.apache.commons.digester3.Digester;
import org.apache.commons.digester3.ObjectCreationFactory;
import org.cucina.engine.DefaultProcessEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;
import org.springframework.expression.AccessException;
import org.springframework.expression.BeanResolver;
import org.springframework.util.Assert;
import org.xml.sax.Attributes;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.10 $
 */
public class ResolverObjectCreationFactory
    implements ObjectCreationFactory<Object> {
    private static final Logger LOG = LoggerFactory.getLogger(ResolverObjectCreationFactory.class);

    /**
     * The default protocol to use to resolve objects. The default is to look
     * for a Spring bean with the id specified in "path" attribute.
     */
    public static final String DEFAULT_PROTOCOL = "bean";
    private BeanResolver beanResolver = DefaultProcessEnvironment.instance().getBeanResolver();
    private Digester digester;

    /**
     * @param digester
     *            The digester to set.
     */
    public void setDigester(Digester digester) {
        this.digester = digester;
    }

    /**
     * @return Returns the digester.
     */
    public Digester getDigester() {
        return digester;
    }

    /**
     * Creates either a condition specified by class and, if not present, ref
     *
     * @param attributes
     *            JAVADOC
     *
     * @return JAVADOC
     */
    public Object createObject(Attributes attributes) {
        Assert.notNull(beanResolver, "beanResolver is null");

        try {
            String path = attributes.getValue("path");

            Object result = beanResolver.resolve(null, path);

            Assert.notNull(result, "Failed to resolve object by path:'" + path + "'");

            // set the id property if it exists to something sensible
            BeanWrapper wrapper = new BeanWrapperImpl(result);

            if ((wrapper.isWritableProperty("id")) && (attributes.getValue("id") != null)) {
                String id = attributes.getValue("id");

                wrapper.setPropertyValue("id", id);
            }

            return result;
        } catch (BeansException e) {
            LOG.error("Oops", e);
        } catch (AccessException e) {
            LOG.error("Oops", e);
        }

        return null;
    }
}
