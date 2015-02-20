
package org.cucina.audit;

import org.eclipse.persistence.config.DescriptorCustomizer;
import org.eclipse.persistence.descriptors.ClassDescriptor;


/**
 * Sets up the <code>HistoryPolicy</code> for the entity.
 *

 *
 */
public class Historised
    implements DescriptorCustomizer {
    /**
     * JAVADOC Method Level Comments
     *
     * @param descriptor
     *            JAVADOC.
     *
     * @throws Exception
     *             JAVADOC.
     * @see org.eclipse.persistence.config.DescriptorCustomizer#customize(org.eclipse.persistence.descriptors.ClassDescriptor)
     */
    @Override
    public void customize(ClassDescriptor descriptor)
        throws Exception {
        descriptor.setHistoryPolicy(new CucinaHistoryPolicy(descriptor.getTableName()));
    }
}
