package org.cucina.engine.server.repository;

import org.cucina.engine.model.ProcessToken;
import org.springframework.data.repository.Repository;

import java.util.Collection;


/**
 * JAVADOC Interface Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public interface ProcessTokenRepository
		extends Repository<ProcessToken, Long> {
	/**
	 * Returns names of workflows and number of active instances.
	 *
	 * @return JAVADOC.
	 */
	Collection<Object[]> countByGroupProcessDefinitionId();

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param wfid JAVADOC.
	 * @return JAVADOC.
	 */
	Collection<ProcessToken> findByProcessDefinitionId(String wfid);
}
