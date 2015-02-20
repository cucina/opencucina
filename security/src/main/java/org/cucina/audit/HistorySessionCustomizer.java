
package org.cucina.audit;

import org.eclipse.persistence.config.SessionCustomizer;
import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.eclipse.persistence.mappings.DatabaseMapping;
import org.eclipse.persistence.mappings.ManyToManyMapping;
import org.eclipse.persistence.sessions.Session;


/**
 * Adds a HistoryPolicy for any ManyToMany mappings where both the source and
 * destination have a HistoryPolicy.

 *
 */
public class HistorySessionCustomizer
    implements SessionCustomizer {
    /**
     * JAVADOC Method Level Comments
     *
     * @param session JAVADOC.
     *
     * @throws Exception JAVADOC.
     */
    @Override
    public void customize(Session session)
        throws Exception {
        for (ClassDescriptor descriptor : session.getProject().getOrderedDescriptors()) {
            if (descriptor.getHistoryPolicy() != null) {
                for (DatabaseMapping mapping : descriptor.getMappings()) {
                    if (mapping.isManyToManyMapping()) {
                        ManyToManyMapping manyToMany = (ManyToManyMapping) mapping;
                        ClassDescriptor referenceDescriptor = session.getClassDescriptor(manyToMany.getReferenceClass());

                        if ((referenceDescriptor.getHistoryPolicy() != null) &&
                                (manyToMany.getHistoryPolicy() == null)) {
                            manyToMany.setHistoryPolicy(new CucinaHistoryPolicy(
                                    manyToMany.getRelationTableName()));
                        }
                    }
                }
            }
        }
    }
}
