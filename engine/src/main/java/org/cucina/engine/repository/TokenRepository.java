package org.cucina.engine.repository;

import org.cucina.engine.model.HistoryRecord;
import org.cucina.engine.model.ProcessToken;
import org.springframework.data.domain.Persistable;
import org.springframework.data.repository.Repository;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;


/**
 * JAVADOC Interface Level
 *
 * @param <T>            JAVADOC.
 * @param <Serializable> JAVADOC.
 * @author $Author: $
 * @version $Revision: $
 */
public interface TokenRepository
		extends Repository<ProcessToken, Long> {
	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param id              JAVADOC.
	 * @param applicationType JAVADOC.
	 * @return JAVADOC.
	 */
	List<HistoryRecord> findHistoryRecordsByDomainObjectIdAndDomainObjectType(Serializable id,
																			  String applicationType);

	/**
	 * Deletes token only.
	 *
	 * @param token JAVADOC.
	 */
	void delete(ProcessToken token);

	/**
	 * Deletes token and domain object.
	 *
	 * @param token JAVADOC.
	 */
	void deleteDeep(ProcessToken token);

	/**
	 * Load tokens for the identified objects of the same type
	 *
	 * @param ids             Collection of objects' ids.
	 * @param applicationType String.
	 * @return JAVADOC.
	 */
	Collection<ProcessToken> findByApplicationTypeAndIds(String applicationType, Serializable... ids);

	/**
	 * Load token for the identified object
	 *
	 * @param domain
	 * @return JAVADOC.
	 */
	ProcessToken findByDomain(Persistable<?> domain);

	/**
	 * Load the ids of domain objects which have this <code>worklfowId</code> and this <code>placeId</code>.
	 *
	 * @param workflowId      String.
	 * @param placeId         String.
	 * @param applicationType String of domains
	 * @return Collection<Object> id of those domain objects meeting the criteria.
	 */
	Collection<Long> findDomainIdsByWorkflowIdPlaceIdApplicationType(String workflowId,
																	 String placeId, String applicationType);

	/**
	 * Creates the Token
	 *
	 * @param token JAVADOC.
	 */
	void save(ProcessToken token);
}
