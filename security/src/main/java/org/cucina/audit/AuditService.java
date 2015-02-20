
package org.cucina.audit;

import java.sql.Timestamp;
import java.util.List;


/**
 * JAVADOC Interface Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public interface AuditService {
    /**
     * JAVADOC Method Level Comments
     *
     * @param applicationType JAVADOC.
     * @param id JAVADOC.
     *
     * @return JAVADOC.
     */
    /**
     * Returns the <code>AuditRecord</code>s for provided applicationType and id.
     *
     * @param id
     * @param applicationType
     * @return
     */
    List<AuditRecord> listAuditRecords(String applicationType, Long id);

    /**
     * JAVADOC Method Level Comments
     *
     * @param applicationType JAVADOC.
     * @param id JAVADOC.
     * @param auditDate JAVADOC.
     *
     * @return JAVADOC.
     */
    /**
     * Returns property that was modified with the new and old
     * value is included for comparison.
     *
     * @param applicationType
     * @param id
     * @param auditDate
     * @return
     */
    List<AuditDifference> listChangeDetails(String applicationType, Long id, Timestamp auditDate);
}
