package org.cucina.engine.definition.config;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.cucina.core.InstanceFactory;
import org.cucina.core.model.Attachment;
import org.cucina.engine.ProcessEnvironment;
import org.cucina.engine.definition.ProcessDefinition;
import org.cucina.engine.model.Workflow;
import org.cucina.engine.model.WorkflowHistory;
import org.cucina.engine.repository.WorkflowRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: vlevine $
 * @version $Revision: 1.6 $
  */
public class ProcessDefinitionRegistryImpl
    implements ProcessDefinitionRegistry {
    private static final Logger LOG = LoggerFactory.getLogger(ProcessDefinitionRegistryImpl.class);
    private InstanceFactory instanceFactory;
    private Map<String, ProcessDefinition> definitionCache = new HashMap<String, ProcessDefinition>();
    private ProcessEnvironment workflowEnvironment;
    private WorkflowRepository workflowRepository;
    private boolean reload = false;

    /**
    * Creates a new WorkflowDefinitionRegistryImpl object.
    *
    * @param workflowEnvironment JAVADOC.
    * @param instanceFactory JAVADOC.
    * @param workflowRepository JAVADOC.
    */
    public ProcessDefinitionRegistryImpl(ProcessEnvironment workflowEnvironment,
        InstanceFactory instanceFactory, WorkflowRepository workflowRepository) {
        Assert.notNull(workflowEnvironment, "cannot provide null workflowEnvironment");
        this.workflowEnvironment = workflowEnvironment;
        Assert.notNull(instanceFactory, "cannot provide null instanceFactory");
        this.instanceFactory = instanceFactory;
        Assert.notNull(workflowRepository, "cannot provide null workflowRepository");
        this.workflowRepository = workflowRepository;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param reload JAVADOC.
     */
    public void setReload(boolean reload) {
        this.reload = reload;
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
            Workflow workflow = workflowRepository.loadByWorkflowId(definitionId);

            if (workflow == null) {
                LOG.info("Cannot find workflow id [" + definitionId + "], return null");

                return null;
            }

            WorkflowHistory latestHistory = workflow.getLatestWorkflowHistory();

            definition = workflowEnvironment.getDefinitionParser()
                                            .parse(new ByteArrayResource(
                        latestHistory.getAttachment().getData()));

            definitionCache.put(definitionId, definition);
        }

        return definition;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param definitionId
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    @Transactional
    public String findWorkflowSource(String definitionId) {
        Workflow workflow = workflowRepository.loadByWorkflowId(definitionId);

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
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public Collection<String> listWorkflowDefinitionIds() {
        return workflowRepository.listAll();
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
            for (Resource workflowResource : resources) {
                ProcessDefinition workflowDefinition = workflowEnvironment.getDefinitionParser()
                                                                          .parse(workflowResource);

                if (workflowDefinition != null) {
                    try {
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("Workflow definition parsed [" + workflowDefinition.getId() +
                                "]");
                        }

                        Workflow workflow = workflowRepository.loadByWorkflowId(workflowDefinition.getId());

                        if (workflow == null) {
                            workflow = instanceFactory.getBean(Workflow.class.getSimpleName());
                            workflow.setWorkflowId(workflowDefinition.getId());
                        }

                        // If reload or a new workflow then persist
                        if (reload || workflow.isNew()) {
                            WorkflowHistory newHistory = instanceFactory.getBean(WorkflowHistory.class.getSimpleName());
                            Attachment attachment = instanceFactory.getBean(Attachment.class.getSimpleName());

                            attachment.setFilename(workflowResource.getFilename());
                            attachment.setData(readBytes(workflowResource));
                            attachment.setType("text/xml");

                            newHistory.setAttachment(attachment);

                            workflow.addHistory(newHistory);

                            workflowRepository.save(workflow);
                            definitionCache.put(workflowDefinition.getId(), workflowDefinition);
                        }
                    } catch (IOException e) {
                        LOG.warn("Failed to load workflowDefinition [" +
                            workflowDefinition.getId() + "]");
                    }
                }
            }
        }
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param definitionId
     *            JAVADOC.
     * @param definition
     *            JAVADOC.
     */
    public void registerWorkflowDefinition(ProcessDefinition workflowDefinition) {
        Assert.notNull(workflowDefinition, "definition cannot be null");
        Assert.notNull(workflowDefinition.getId(), "definition's id cannot be null");

        definitionCache.put(workflowDefinition.getId(), workflowDefinition);
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
}
