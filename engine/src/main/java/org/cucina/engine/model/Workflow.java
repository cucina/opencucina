package org.cucina.engine.model;

import org.apache.commons.collections.CollectionUtils;
import org.cucina.core.model.PersistableEntity;
import org.cucina.core.model.Versioned;
import org.cucina.core.validation.Delete;
import org.cucina.core.validation.Update;
import org.cucina.engine.validation.WorkflowExists;
import org.cucina.engine.validation.WorkflowPlacesRetained;
import org.cucina.engine.validation.WorkflowUnused;

import javax.persistence.*;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


/**
 * JAVADOC Interface Level
 *
 * @author $Author: vlevine $
 * @version $Revision: 1.1 $
 */
@Entity
@Cacheable
@WorkflowPlacesRetained(groups = Update.class)
public class Workflow
		extends PersistableEntity
		implements Versioned {
	private static final long serialVersionUID = -4142311754174475756L;
	@Valid
	private List<WorkflowHistory> workflowHistories;
	@WorkflowUnused(groups = Delete.class)
	@WorkflowExists(groups = Update.class)
	private String workflowId;
	private int version;

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	@Transient
	public WorkflowHistory getLatestWorkflowHistory() {
		return CollectionUtils.isEmpty(getWorkflowHistories()) ? null
				: getWorkflowHistories()
				.get(getWorkflowHistories()
						.size() - 1);
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	@Version
	public int getVersion() {
		return version;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param version JAVADOC.
	 */
	public void setVersion(int version) {
		this.version = version;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "workflow")
	@OrderColumn(insertable = true, updatable = true)
	public List<WorkflowHistory> getWorkflowHistories() {
		return workflowHistories;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param workflowHistories JAVADOC.
	 */
	public void setWorkflowHistories(List<WorkflowHistory> workflowHistories) {
		this.workflowHistories = workflowHistories;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	@Column(unique = true, nullable = false)
	public String getWorkflowId() {
		return workflowId;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param workflowId JAVADOC.
	 */
	public void setWorkflowId(String workflowId) {
		this.workflowId = workflowId;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param newHistory JAVADOC.
	 */
	public void addHistory(WorkflowHistory newHistory) {
		if (getWorkflowHistories() == null) {
			setWorkflowHistories(new ArrayList<WorkflowHistory>());
		}

		getWorkflowHistories().add(newHistory);
		newHistory.setWorkflow(this);
	}
}
