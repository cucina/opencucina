package org.cucina.engine.model;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.cucina.core.model.Attachment;
import org.cucina.core.model.PersistableEntity;
import org.cucina.core.validation.Create;
import org.cucina.core.validation.Update;

import org.cucina.engine.definition.ProcessDefinition;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: vlevine $
 */
@Entity
@Cacheable
public class WorkflowHistory
    extends PersistableEntity {
    private static final long serialVersionUID = 6549232242846422940L;

    /** attachment */
    public static final String ATTACHMENT_FIELD_NAME = "attachment";
    @NotNull(groups =  {
        Create.class, Update.class}
    , message = "org.cucina.engine.validation.null.data")
    private Attachment attachment;
    @Valid
    private ProcessDefinition workflowDefinition;
    private Workflow workflow;

    /**
     * JAVADOC Method Level Comments
     *
     * @param attachment
     *            JAVADOC.
     */
    public void setAttachment(Attachment attachment) {
        this.attachment = attachment;
    }

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
     * @param workflow
     *            JAVADOC.
     */
    public void setWorkflow(Workflow workflow) {
        this.workflow = workflow;
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
     * @param workflowDefinition
     *            JAVADOC.
     */
    public void setWorkflowDefinition(ProcessDefinition workflowDefinition) {
        this.workflowDefinition = workflowDefinition;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Transient
    public ProcessDefinition getWorkflowDefinition() {
        return workflowDefinition;
    }
}
