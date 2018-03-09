package org.cucina.engine.service;

import java.util.Collection;
import java.util.Map;


/**
 * Provides ability to execute bulk actions on workflow.
 *
 * @author $Author: $
 * @version $Revision: $
 */
public interface BulkWorkflowService {
	/**
	 * Execute transitions for those workflowed items with
	 * <code>workflowId</code> and in place corresponding to transition
	 * <code>transitionId</code>.
	 *
	 * @param applicationType applicationType of objects.
	 * @param workflowId      String workflowId of items to be transitioned.
	 * @param transitionId    String id of the transition to run.
	 * @param parameters      Map<String, Object> to be passed to workflow session.
	 */
	void executeTransitions(String applicationType, String workflowId, String transitionId,
							Map<String, Object> parameters);

	/**
	 * Execute transitions for those workflowed items identified by ids, with
	 * <code>applicationType</code> and/or worklfow with <code>workflowId</code>
	 * and in place corresponding to transition <code>transitionId</code>.
	 *
	 * @param ids             IDs of objects to be transitioned.
	 * @param applicationType applicationType of objects.
	 * @param workflowId      String workflowId of items to be transitioned.
	 * @param transitionId    String id of the transition to run.
	 * @param parameters      Map<String, Object> to be passed to workflow session.
	 */
	void executeTransitions(Collection<Long> ids, String applicationType, String workflowId,
							String transitionId, Map<String, Object> parameters);
}
