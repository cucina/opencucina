package org.cucina.engine.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindException;

import org.cucina.core.InstanceFactory;
import org.cucina.core.model.Attachment;

import org.cucina.engine.definition.ProcessDefinition;
import org.cucina.engine.definition.config.ProcessDefinitionRegistry;
import org.cucina.engine.model.Workflow;


/**
 * JAVADOC for Class Level
 *
 * @author vlevine
 */
@Service
public class DefinitionServiceImpl
    implements DefinitionService {
    private InstanceFactory instanceFactory;
    private ProcessDefinitionRegistry processDefinitionRegistry;

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
    public DefinitionServiceImpl(ProcessDefinitionRegistry processDefinitionRegistry,
        InstanceFactory instanceFactory) {
        Assert.notNull(processDefinitionRegistry, "processDefinitionRegistry is null");
        this.processDefinitionRegistry = processDefinitionRegistry;
        Assert.notNull(instanceFactory, "instanceFactory is null");
        this.instanceFactory = instanceFactory;
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
        return processDefinitionRegistry.createProcess(createAttachment(fileName, contentType,
                content));
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
        processDefinitionRegistry.delete(id);
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
        return processDefinitionRegistry.findWorkflowDefinition(id);
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
    @Override
    public Workflow update(String fileName, String contentType, byte[] content)
        throws BindException {
        return processDefinitionRegistry.updateProcess(createAttachment(fileName, contentType,
                content));
    }

    private Attachment createAttachment(String fileName, String contentType, byte[] content) {
        Attachment attachment = instanceFactory.getBean(Attachment.class.getSimpleName());

        attachment.setFilename(fileName);
        attachment.setData(content);
        attachment.setType(contentType);

        return attachment;
    }
}
