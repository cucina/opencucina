package org.cucina.engine.service;

import org.cucina.core.model.Attachment;
import org.cucina.engine.definition.Token;
import org.cucina.engine.definition.Transition;
import org.cucina.engine.model.HistoryRecord;
import org.cucina.i18n.api.ListItemDto;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * A general interface to call workfow system for all most common uses
 *
 * @author vlevine
 */
public interface ProcessSupportService {
	/**
	 * For a given entire workflow, return a collection of all transitions that
	 * a user (rather than an application) might make. These may or may not have
	 * privileges set on them. The list will exclude system-only transitions.
	 *
	 * @param workflowId JAVADOC.
	 * @return JAVADOC.
	 */
	Collection<Transition> listActionableTransitions(String workflowId);

	/**
	 * Extract names of possible transitions for these objects. Assumption is
	 * that these objects are not in the same workflow place.
	 *
	 * @param ids             JAVADOC.
	 * @param applicationType JAVADOC.
	 * @return JAVADOC.
	 */
	Map<Serializable, Collection<String>> listAllTransitions(Collection<Serializable> ids, String applicationType);

	/**
	 * Extract names of possible transitions for these objects. Assumption is
	 * that these objects are in the same workflow place.
	 *
	 * @param ids             JAVADOC.
	 * @param applicationType JAVADOC.
	 * @return JAVADOC.
	 */
	Collection<String> listTransitions(Collection<Serializable> ids, String applicationType);

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param ids             JAVADOC.
	 * @param applicationType JAVADOC.
	 * @return JAVADOC.
	 */
	Collection<Map<String, Object>> listWorkflowProperties(Collection<Serializable> ids,
														   String applicationType);

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param workflowId JAVADOC.
	 * @return JAVADOC.
	 */
	Collection<Map<String, String>> loadTransitionInfo(String workflowId);

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param entities        Map of ids vs version.
	 * @param applicationType JAVADOC.
	 * @param transitionId    JAVADOC.
	 * @param comment         JAVADOC.
	 * @param approvedAs      JAVADOC.
	 * @param assignedTo      JAVADOC.
	 * @param extraParams     JAVADOC.
	 * @param listNodeDto     JAVADOC.
	 * @param attachment      JAVADOC.
	 */
	void makeBulkTransition(Map<Serializable, Integer> entities, String applicationType, String transitionId,
							String comment, String approvedAs, String assignedTo, Map<String, Object> extraParams,
							ListItemDto listNodeDto, Attachment attachment);

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param id              JAVADOC.
	 * @param applicationType JAVADOC.
	 * @param comment         JAVADOC.
	 * @param approvedAs      JAVADOC.
	 * @param assignedTo      JAVADOC.
	 * @param extraParams     JAVADOC.
	 * @param attachment      JAVADOC.
	 */
	void makeTransition(Serializable id, String applicationType, String transitionId, String comment,
						String approvedAs, String assignedTo, Map<String, Object> extraParams, Attachment attachment);

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param id              JAVADOC.
	 * @param applicationType JAVADOC.
	 * @return JAVADOC.
	 */
	List<HistoryRecord> obtainHistory(Serializable id, String applicationType);

	/**
	 * Obtain a summary of the history information.
	 *
	 * @param id              JAVADOC.
	 * @param applicationType JAVADOC.
	 * @return JAVADOC.
	 */
	List<Map<Object, Object>> obtainHistorySummary(Serializable id, String applicationType);

	/**
	 * Starts and persists workflow and entity
	 *
	 * @param entity     PersistableEntity.
	 * @param workflowId workflowName
	 * @param parameters
	 * @return JAVADOC.
	 */
	Token startWorkflow(Object entity, String workflowId, Map<String, Object> parameters);
}
