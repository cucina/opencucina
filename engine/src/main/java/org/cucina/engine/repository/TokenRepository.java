package org.cucina.engine.repository;

import java.util.Collection;

import org.cucina.engine.model.WorkflowToken;
import org.springframework.data.domain.Persistable;
import org.springframework.data.repository.Repository;


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
     * Creates the Token
     *
     * @param token JAVADOC.
     */
    void create(WorkflowToken token);

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
     * Load the ids of domain objects which have this <code>worklfowId</code> and this <code>placeId</code>.
     *
     * @param workflowId String.
     * @param placeId String.
     * @param applicationType String of domains
     *
     * @return Collection<Object> id of those domain objects meeting the criteria.
     */
    Collection<Long> loadDomainIds(String workflowId, String placeId, String applicationType);

    /**
    * Load token for the identified object
    *
    * @param domain
    *
    * @return JAVADOC.
    */
    WorkflowToken loadToken(Persistable<Long> domain);

    /**
     * Load tokens for the identified objects of the same type
     *
     * @param ids Collection of objects' ids.
     * @param applicationType String.
     *
     * @return JAVADOC.
     */
    Collection<WorkflowToken> loadTokens(String applicationType, Long... ids);

    /**
     * Updates the Token
     *
     * @param token JAVADOC.
     */
    void update(WorkflowToken token);
}
