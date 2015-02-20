
package org.cucina.core.model;

import javax.persistence.Version;


/**
 * Required by those objects whose need to have version control applied. Note
 * the annotation <code>@Version</code> which need to be set on the field or a
 * property of the class implementing this interface for EntityManager to take the field into account.
 *
 * @author $Author: $
 * @version $Revision: $
 */
public interface Versioned {
    String PROPERTY_VERSION = "version";

    /**
     * Set version
     *
     * @param version
     *            int.
     */
    void setVersion(int version);

    /**
     * Get version
     *
     * @return version.
     */
    @Version
    int getVersion();
}
