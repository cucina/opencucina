package org.cucina.search;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Implementation which delegates matching of attribute values to the
 * appropriate <code>AttributeRestrictor</code>.
 *
 */
public class ConfigurableAttributeMatcher
    implements AttributeMatcher {
    private static final Logger LOG = LoggerFactory.getLogger(ConfigurableAttributeMatcher.class);

    /**
     * Contains a list of available <code>AttributeRestrictor</code>s
     */
    private List<AttributeRestrictor> attributeRestrictors;

    /**
     * JAVADOC Method Level Comments
     *
     * @param attributeRestrictors
     *            JAVADOC.
     */
    public void setAttributeRestrictors(List<AttributeRestrictor> attributeRestrictors) {
        this.attributeRestrictors = attributeRestrictors;
    }

    /**
     * Uses the configured
     * <code>AttributeRestrictor<code> for the attribute type, if none
     * is found a simple equals is performed on property.
     */
    @Override
    public boolean match(Map<String, Object> attributes, Map<String, Object> restrictions) {
        boolean match = true;

        if (MapUtils.isNotEmpty(restrictions)) {
            if (MapUtils.isNotEmpty(attributes)) {
                for (Map.Entry<String, Object> entry : restrictions.entrySet()) {
                    Object attribute = attributes.get(entry.getKey());
                    Object restriction = entry.getValue();

                    if (restriction != null) {
                        if (attribute != null) {
                            AttributeRestrictor restrictor = findRestrictor(attribute.getClass());

                            if (restrictor != null) {
                                match = restrictor.test(attribute, restriction);
                            } else {
                                match = attribute.equals(restriction);
                            }
                        } else {
                            match = false;
                        }
                    }
                }
            } else {
                if (LOG.isDebugEnabled()) {
                    LOG.debug(
                        "No attributes available so could not match the provided restrictions : " +
                        restrictions);
                }

                match = false;
            }
        }

        if (!match) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(attributes + " not valid for restrictions " + restrictions);
            }
        }

        return match;
    }

    /**
     * Loop through the provided <code>AttributeRestrictor</code>s and return
     * the first one that supports the required type.
     *
     * @param clazz
     * @return
     */
    private AttributeRestrictor findRestrictor(Class<?> clazz) {
        for (AttributeRestrictor restrictor : attributeRestrictors) {
            if (restrictor.supports(clazz)) {
                return restrictor;
            }
        }

        return null;
    }
}
