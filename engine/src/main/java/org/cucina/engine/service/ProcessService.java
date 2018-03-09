package org.cucina.engine.service;

import org.cucina.engine.definition.Token;

import java.util.Collection;
import java.util.Map;


/**
 * JAVADOC Interface Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public interface ProcessService {
	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param token        JAVADOC.
	 * @param transitionId JAVADOC.
	 * @param parameters   JAVADOC.
	 * @return JAVADOC.
	 */
	Token executeTransition(Token token, String transitionId, Map<String, Object> parameters);

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param token      JAVADOC.
	 * @param parameters JAVADOC.
	 * @return JAVADOC.
	 */
	Collection<String> listTransitions(Token token, Map<String, Object> parameters);

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param object       JAVADOC.
	 * @param workflowId   JAVADOC.
	 * @param transitionId JAVADOC.
	 * @param parameters   JAVADOC.
	 * @return JAVADOC.
	 */
	Token startProcess(Object object, String workflowId, String transitionId,
					   Map<String, Object> parameters);
}
