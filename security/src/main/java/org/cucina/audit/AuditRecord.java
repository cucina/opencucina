
package org.cucina.audit;

import java.sql.Timestamp;


/**
 * Holds audit information.

 *
 */
public class AuditRecord {
    private String modifiedDate;
    private String action;
    private String modifiedBy;

    /**
     * JAVADOC Method Level Comments
     *
     * @param action JAVADOC.
     */
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public String getAction() {
        return action;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param modifiedBy JAVADOC.
     */
    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public String getModifiedBy() {
        return modifiedBy;
    }

    /**
     * Uses the supplied <code>Timestamp</code> and stores it's <code>String</code> representation in 
     * the format yyyy-mm-dd hh:mm:ss.fffffffff
     *
     * @param modifiedDate JAVADOC.
     */
    public void setModifiedDate(Timestamp modifiedDate) {
        this.modifiedDate = modifiedDate.toString();
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public String getModifiedDate() {
        return modifiedDate;
    }
    public static enum Operation {INSERT, UPDATE, DELETE;
    }
}
