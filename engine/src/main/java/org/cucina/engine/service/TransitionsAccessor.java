package org.cucina.engine.service;

import org.cucina.core.model.PersistableEntity;

import java.util.Collection;
import java.util.Map;


/**
 * JAVADOC Interface Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public interface TransitionsAccessor {
	/**
	 * Extract names of permitted transitions for this place and a workflowed object for the currently authenticated User. Uses a map of
	 * properties to values
	 *
	 * @param workflowDefinitionId String.
	 * @param row
	 * @param applicationType      String.
	 * @return JAVADOC.
	 */
	public Collection<String> listPermittedTransitions(String workflowDefinitionId, String placeId,
													   String applicationType, Map<String, Object> map);

	/**
	 * Extract names of permitted transitions for this place and a workflowed object for the currently authenticated User.
	 * Uses a PersistableEntity
	 * *
	 *
	 * @param workflowDefinitionId JAVADOC.
	 * @param placeId              JAVADOC.
	 * @param entity               JAVADOC.
	 * @return JAVADOC.
	 */
	public Collection<String> listPermittedTransitions(String workflowDefinitionId, String placeId,
													   PersistableEntity entity);

	/**
	 * Similar to listPermittedTransitions however it only checks that the user has a certain privilege, it does not check against the properties of the object
	 * being returned in search relative to a user's dimensions in their permissions.
	 * <p>
	 * Use with care.
	 *
	 * @param workflowDefinitionId JAVADOC.
	 * @param placeId              JAVADOC.
	 * @return JAVADOC.
	 */
	public Collection<String> listPermittedTransitionsNoObjectCheck(String workflowDefinitionId,
																	String placeId);
}
