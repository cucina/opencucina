package org.cucina.engine.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Persistable;
import org.springframework.data.repository.Repository;

import org.cucina.engine.model.HistoryRecord;
import org.cucina.engine.model.WorkflowToken;


/**
 * JAVADOC Interface Level
 *
 * @author $Author: $
 * @version $Revision: $
  *
 * @param <T> JAVADOC.
 * @param <Serializable> JAVADOC.
 */
public interface TokenRepository
    extends Repository<WorkflowToken, Long> {
    /**
     * JAVADOC Method Level Comments
     *
     * @param id JAVADOC.
     * @param applicationType JAVADOC.
     *
     * @return JAVADOC.
     */
    List<HistoryRecord> findHistoryRecordsByDomainObjectIdAndDomainObjectType(Long id,
        String applicationType);

    /**
     * Deletes token only.
     *
     * @param token JAVADOC.
     */
    void delete(WorkflowToken token);

    /**
     * Deletes token and domain object.
     *
     * @param token JAVADOC.
     */
    void deleteDeep(WorkflowToken token);

    /**
     * Load tokens for the identified objects of the same type
     *
     * @param ids Collection of objects' ids.
     * @param applicationType String.
     *
     * @return JAVADOC.
     */
    Collection<WorkflowToken> findByApplicationTypeAndIds(String applicationType, Long... ids);

    /**
    * Load token for the identified object
    *
    * @param domain
    *
    * @return JAVADOC.
    */
    WorkflowToken findByDomain(Persistable<Long> domain);

    /**
     * Load the ids of domain objects which have this <code>worklfowId</code> and this <code>placeId</code>.
     *
     * @param workflowId String.
     * @param placeId String.
     * @param applicationType String of domains
     *
     * @return Collection<Object> id of those domain objects meeting the criteria.
     */
    Collection<Long> findDomainIdsByWorkflowIdPlaceIdApplicationType(String workflowId,
        String placeId, String applicationType);

    /**
     * Creates the Token
     *
     * @param token JAVADOC.
     */
    void save(WorkflowToken token);
}
