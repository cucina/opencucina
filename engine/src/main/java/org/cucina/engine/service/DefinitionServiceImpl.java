package org.cucina.engine.service;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.cucina.core.InstanceFactory;
import org.cucina.core.model.Attachment;
import org.cucina.core.validation.Create;
import org.cucina.core.validation.Delete;
import org.cucina.core.validation.Update;
import org.cucina.engine.ProcessEnvironment;
import org.cucina.engine.definition.ProcessDefinition;
import org.cucina.engine.model.Workflow;
import org.cucina.engine.model.WorkflowHistory;
import org.cucina.engine.repository.WorkflowRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindException;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: vlevine $
 */
@Service
public class DefinitionServiceImpl
    implements DefinitionService {
    private static final Logger LOG = LoggerFactory.getLogger(DefinitionServiceImpl.class);
    private InstanceFactory instanceFactory;
    private ProcessEnvironment processEnvironment;
    private Validator validator;
    private WorkflowRepository workflowRepository;

    /**
     * Creates a new DefinitionServiceImpl object.
     *
     * @param processEnvironment
     *            JAVADOC.
     * @param instanceFactory
     *            JAVADOC.
     * @param validator
     *            JAVADOC.
     * @param workflowRepository
     *            JAVADOC.
     */
    @Autowired
    public DefinitionServiceImpl(ProcessEnvironment processEnvironment,
        InstanceFactory instanceFactory, WorkflowRepository workflowRepository) {
        Assert.notNull(processEnvironment, "workflowEnvironment is null");
        this.processEnvironment = processEnvironment;
        Assert.notNull(instanceFactory, "instanceFactory is null");
        this.instanceFactory = instanceFactory;
        Assert.notNull(workflowRepository, "workflowRepository is null");
        this.workflowRepository = workflowRepository;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param validator
     *            JAVADOC.
     */
    public void setValidator(Validator validator) {
        this.validator = validator;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param fileName
     *            JAVADOC.
     * @param contentType
     *            JAVADOC.
     * @param content
     *            JAVADOC.
     *
     * @return JAVADOC.
     *
     * @throws BindException
     *             JAVADOC.
     */
    public Workflow create(String fileName, String contentType, byte[] content)
        throws BindException {
        WorkflowHistory newHistory = createHistory(fileName, contentType, content);
        Workflow workflow = instanceFactory.getBean(Workflow.class.getSimpleName());

        workflow.addHistory(newHistory);

        validate(workflow, Create.class);

        workflow.setWorkflowId(newHistory.getWorkflowDefinition().getId());
        processEnvironment.getDefinitionRegistry()
                           .registerWorkflowDefinition(newHistory.getWorkflowDefinition());

        workflowRepository.save(workflow);

        return workflow;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param id
     *            JAVADOC.
     *
     * @throws BindException
     *             JAVADOC.
     */
    public void delete(String id)
        throws BindException {
        Workflow wf = workflowRepository.loadByWorkflowId(id);

        validate(wf, Delete.class);

        // TODO enhance workflowRepository to support this
        // TODO gain access to workflow registry and delete from it as well
        //workflowEnvironment.getDefinitionRegistry().unregister(id);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param id JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public ProcessDefinition loadDefinition(String id) {
        return processEnvironment.getDefinitionRegistry().findWorkflowDefinition(id);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param id
     *            JAVADOC.
     * @param fileName
     *            JAVADOC.
     * @param contentType
     *            JAVADOC.
     * @param content
     *            JAVADOC.
     *
     * @return JAVADOC.
     *
     * @throws BindException
     *             JAVADOC.
     */
    public Workflow update(String id, String fileName, String contentType, byte[] content)
        throws BindException {
        Workflow workflow = workflowRepository.loadByWorkflowId(id);

        Assert.notNull(workflow, "Failed to load workflow with id=" + id);

        WorkflowHistory newHistory = createHistory(fileName, contentType, content);

        if (!workflow.getWorkflowId().equals(newHistory.getWorkflowDefinition().getId())) {
            BindException be = new BindException(workflow, workflow.getApplicationType());

            be.reject("workflow.validation.failure",
                "Id in file '" + newHistory.getWorkflowDefinition().getId() +
                "' is not the same as of workflow '" + workflow.getWorkflowId() + "'");
            throw be;
        }

        workflow.addHistory(newHistory);
        validate(workflow, Update.class);

        workflowRepository.save(workflow);
        processEnvironment.getDefinitionRegistry()
                           .registerWorkflowDefinition(newHistory.getWorkflowDefinition());

        return workflow;
    }

    private WorkflowHistory createHistory(String fileName, String contentType, byte[] content) {
        WorkflowHistory newHistory = instanceFactory.getBean(WorkflowHistory.class.getSimpleName());

        Attachment attachment = instanceFactory.getBean(Attachment.class.getSimpleName());

        attachment.setFilename(fileName);
        attachment.setData(content);
        attachment.setType(contentType);

        newHistory.setAttachment(attachment);

        ProcessDefinition definition = processEnvironment.getDefinitionParser()
                                                          .parse(new ByteArrayResource(content));

        Assert.notNull(definition, "failed to parse definition from file '" + fileName + "'");

        if (LOG.isDebugEnabled()) {
            LOG.debug("Parsed workflowDefinition with id:" + definition.getId());
        }

        newHistory.setWorkflowDefinition(definition);

        return newHistory;
    }

    private void validate(Workflow workflow, Class<?> group)
        throws BindException {
        if (validator == null) {
            return;
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Validating for group " + group.getName() + " workflow:" + workflow);
        }

        Set<ConstraintViolation<Workflow>> violations = validator.validate(workflow, group);

        if (!violations.isEmpty()) {
            // validation errors
            BindException be = new BindException(workflow, workflow.getApplicationType());

            for (ConstraintViolation<Workflow> constraintViolation : violations) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("ConstraintViolation:" + constraintViolation);
                }

                be.reject(constraintViolation.getMessageTemplate(),
                    new Object[] { constraintViolation.getLeafBean() },
                    constraintViolation.getMessage());
            }

            throw be;
        }

        return;
    }
}
