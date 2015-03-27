package org.cucina.engine.definition.config;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.apache.commons.collections.CollectionUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindException;

import org.cucina.core.InstanceFactory;
import org.cucina.core.model.Attachment;
import org.cucina.core.validation.Create;
import org.cucina.core.validation.Delete;
import org.cucina.core.validation.Update;

import org.cucina.engine.definition.ProcessDefinition;
import org.cucina.engine.model.Workflow;
import org.cucina.engine.model.WorkflowHistory;
import org.cucina.engine.repository.WorkflowRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * JAVADOC for Class Level
 *
 * @author vlevine
 */
@ManagedResource
@Component
public class ProcessDefinitionRegistryImpl
    implements ProcessDefinitionRegistry {
    private static final Logger LOG = LoggerFactory.getLogger(ProcessDefinitionRegistryImpl.class);
    private InstanceFactory instanceFactory;
    private Map<String, ProcessDefinition> definitionCache = new HashMap<String, ProcessDefinition>();
    private ProcessDefinitionParser definitionParser;
    private Validator validator;
    private WorkflowRepository workflowRepository;

    // TODO make option to reload on startup
    //private boolean reload = false;

    /**
     * Creates a new WorkflowDefinitionRegistryImpl object.
     *
     * @param workflowEnvironment
     *            JAVADOC.
     * @param instanceFactory
     *            JAVADOC.
     * @param workflowRepository
     *            JAVADOC.
     */
    @Autowired
    public ProcessDefinitionRegistryImpl(InstanceFactory instanceFactory,
        WorkflowRepository workflowRepository, ProcessDefinitionParser definitionParser) {
        Assert.notNull(instanceFactory, "instanceFactory is null");
        this.instanceFactory = instanceFactory;
        Assert.notNull(workflowRepository, "workflowRepository is null");
        this.workflowRepository = workflowRepository;
        Assert.notNull(definitionParser, "definitionParser is null");
        this.definitionParser = definitionParser;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param reload
     *            JAVADOC.
     */

    /* public void setReload(boolean reload) {
         this.reload = reload;
     }*/

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
     * @param definitionId
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    @ManagedOperation
    @Override
    public String getWorkflowSource(String definitionId) {
        Workflow workflow = workflowRepository.findByWorkflowId(definitionId);

        if (workflow == null) {
            LOG.warn("Cannot find workflow id [" + definitionId + "], return null");

            return null;
        }

        WorkflowHistory latestHistory = workflow.getLatestWorkflowHistory();

        Assert.notNull(latestHistory, "The workflow '" + definitionId + "' does not have history!");

        try {
            return new String(latestHistory.getAttachment().getData(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOG.error("Failed to convert byte array to String", e);

            return null;
        }
    }

    /**
     *
     *
     * @param attachment .
     *
     * @throws BindException .
     */
    @Override
    public Workflow createProcess(Attachment attachment)
        throws BindException {
        WorkflowHistory newHistory = instanceFactory.getBean(WorkflowHistory.class.getSimpleName());

        newHistory.setAttachment(attachment);

        ProcessDefinition definition = definitionParser.parse(new ByteArrayResource(
                    attachment.getData()));

        newHistory.setProcessDefinition(definition);

        Workflow workflow = instanceFactory.getBean(Workflow.class.getSimpleName());

        workflow.addHistory(newHistory);
        Assert.notNull(workflow, "workflow is null");

        if (workflow.getWorkflowId() == null) {
            workflow.setWorkflowId(workflow.getLatestWorkflowHistory().getProcessDefinition().getId());
        }

        Assert.notNull(workflow.getWorkflowId(), "Workflow id is null");

        if (LOG.isDebugEnabled()) {
            LOG.debug("Adding workflow with id:" + workflow.getWorkflowId());
        }

        validate(workflow, Create.class);
        definitionCache.put(workflow.getWorkflowId(),
            workflow.getLatestWorkflowHistory().getProcessDefinition());
        workflowRepository.save(workflow);

        return workflow;
    }

    /**
     *
     *
     * @param workflowId .
     */
    @Override
    @Transactional
    public void delete(String workflowId)
        throws BindException {
        Workflow wf = workflowRepository.findByWorkflowId(workflowId);

        validate(wf, Delete.class);

        definitionCache.remove(workflowId);
        workflowRepository.delete(workflowId);
    }

    /**
     * Find workflow definition from local cache or failing that load up.
     * Synchronised so it will do parsing only once per definitionId since
     * parser is not thread-safe.
     *
     * @param definitionId
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    public synchronized ProcessDefinition findWorkflowDefinition(String definitionId) {
        ProcessDefinition definition = definitionCache.get(definitionId);

        if (definition == null) {
            Workflow workflow = workflowRepository.findByWorkflowId(definitionId);

            if (workflow == null) {
                LOG.info("Cannot find workflow id [" + definitionId + "], return null");

                return null;
            }

            WorkflowHistory latestHistory = workflow.getLatestWorkflowHistory();

            definition = definitionParser.parse(new ByteArrayResource(
                        latestHistory.getAttachment().getData()));

            definitionCache.put(definitionId, definition);
        }

        return definition;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @ManagedOperation
    @Override
    public Collection<String> listWorkflowDefinitionIds() {
        return workflowRepository.findAllIds();
    }

    /**
     * Reads workflow definitions from the provided list of resources.
     *
     * @param resources
     *            JAVADOC.
     */
    @Transactional
    public void readWorkflowDefinitions(Collection<Resource> resources) {
        Assert.notNull(resources, "cannot provide null workflowResource");

        if (CollectionUtils.isNotEmpty(resources)) {
            for (Resource resource : resources) {
                String filename = resource.getFilename();

                try {
                    Attachment attachment = instanceFactory.getBean(Attachment.class.getSimpleName());

                    attachment.setFilename(filename);
                    attachment.setData(readBytes(resource));
                    attachment.setType("text/xml");

                    createProcess(attachment);
                } catch (IOException e) {
                    if (LOG.isDebugEnabled()) {
                        LOG.warn("Failed to load workflowDefinition [" + filename + "]", e);
                    } else {
                        LOG.warn("Failed to load workflowDefinition [" + filename + "]");
                    }
                } catch (BindException e) {
                    if (LOG.isDebugEnabled()) {
                        LOG.warn("Failed validation for [" + filename + "]", e);
                    } else {
                        LOG.warn("Failed validation for [" + filename + "]");
                    }
                }
            }
        }
    }

    /**
     *
     *
     * @param history .
     */
    @Override
    @Transactional
    public Workflow updateProcess(Attachment attachment)
        throws BindException {
        ProcessDefinition definition = definitionParser.parse(new ByteArrayResource(
                    attachment.getData()));
        Workflow workflow = workflowRepository.findByWorkflowId(definition.getId());

        Assert.notNull(workflow, "Failed to load workflow with id " + definition.getId());

        WorkflowHistory newHistory = instanceFactory.getBean(WorkflowHistory.class.getSimpleName());

        newHistory.setAttachment(attachment);

        Assert.notNull(definition,
            "failed to parse definition '" + definition.getId() + "' from file '" +
            attachment.getFilename() + "'");

        if (LOG.isDebugEnabled()) {
            LOG.debug("Parsed workflowDefinition with id:" + definition.getId());
        }

        newHistory.setProcessDefinition(definition);

        workflow.addHistory(newHistory);

        validate(workflow, Update.class);

        definitionCache.put(workflow.getWorkflowId(), newHistory.getProcessDefinition());
        workflowRepository.save(workflow);

        return workflow;
    }

    private byte[] readBytes(Resource resource)
        throws IOException {
        InputStream is = null;

        try {
            is = resource.getInputStream();

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[16384];

            while ((nRead = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }

            buffer.flush();

            return buffer.toByteArray();
        } finally {
            if (is != null) {
                is.close();
            }
        }
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
