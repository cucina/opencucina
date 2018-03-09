package org.cucina.engine.model;

import org.cucina.core.model.Attachment;
import org.cucina.core.model.PersistableEntity;
import org.cucina.core.validation.Create;
import org.cucina.core.validation.Update;
import org.cucina.engine.definition.ProcessDefinition;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: vlevine $
 */
@Entity
@Cacheable
public class WorkflowHistory
		extends PersistableEntity {
	/**
	 * attachment
	 */
	public static final String ATTACHMENT_FIELD_NAME = "attachment";
	private static final long serialVersionUID = 6549232242846422940L;
	@NotNull(groups = {
			Create.class, Update.class}
			, message = "org.cucina.engine.validation.null.data")
	private Attachment attachment;
	@Valid
	private ProcessDefinition processDefinition;
	private Workflow workflow;

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "attachment", nullable = false)
	public Attachment getAttachment() {
		return attachment;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param attachment JAVADOC.
	 */
	public void setAttachment(Attachment attachment) {
		this.attachment = attachment;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	@ManyToOne
	@JoinColumn(name = "worklfow", nullable = false)
	public Workflow getWorkflow() {
		return workflow;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param workflow JAVADOC.
	 */
	public void setWorkflow(Workflow workflow) {
		this.workflow = workflow;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	@Transient
	public ProcessDefinition getProcessDefinition() {
		return processDefinition;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param processDefinition JAVADOC.
	 */
	public void setProcessDefinition(ProcessDefinition processDefinition) {
		this.processDefinition = processDefinition;
	}
}
