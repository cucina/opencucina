package org.cucina.engine.definition.config;

import org.apache.commons.collections.CollectionUtils;
import org.cucina.core.InstanceFactory;
import org.cucina.core.model.Attachment;
import org.cucina.engine.definition.ProcessDefinition;
import org.cucina.engine.model.Workflow;
import org.cucina.engine.model.WorkflowHistory;
import org.cucina.engine.repository.WorkflowRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@ManagedResource
@Component
public class ProcessDefinitionRegistryImpl
		implements ProcessDefinitionRegistry {
	private static final Logger LOG = LoggerFactory.getLogger(ProcessDefinitionRegistryImpl.class);
	private InstanceFactory instanceFactory;
	private Map<String, ProcessDefinition> definitionCache = new HashMap<String, ProcessDefinition>();
	private ProcessDefinitionParser definitionParser;
	private WorkflowRepository workflowRepository;

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

	@Override
	@Transactional
	public void delete(String workflowId) {
		definitionCache.remove(workflowId);
		workflowRepository.delete(workflowId);
	}

	/**
	 * Find workflow definition from local cache or failing that load up.
	 * Synchronised so it will do parsing only once per definitionId since
	 * parser is not thread-safe.
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

	@ManagedOperation
	@Override
	public Collection<String> listWorkflowDefinitionIds() {
		return workflowRepository.findAllIds();
	}

	/**
	 * Reads workflow definitions from the provided list of resources.
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

					saveProcess(attachment);
				} catch (IOException e) {
					if (LOG.isDebugEnabled()) {
						LOG.warn("Failed to load workflowDefinition [" + filename + "]", e);
					} else {
						LOG.warn("Failed to load workflowDefinition [" + filename + "]");
					}
				}
			}
		}
	}

	@Override
	@Transactional
	public Workflow saveProcess(Attachment attachment) {
		ProcessDefinition definition = definitionParser.parse(new ByteArrayResource(
				attachment.getData()));

		Assert.notNull(definition,
				"failed to parse definition '" + definition.getId() + "' from file '" +
						attachment.getFilename() + "'");

		if (LOG.isDebugEnabled()) {
			LOG.debug("Parsed workflowDefinition with id:" + definition.getId());
		}

		Workflow workflow = workflowRepository.findByWorkflowId(definition.getId());

		if (workflow == null) {
			workflow = instanceFactory.getBean(Workflow.class.getSimpleName());
			workflow.setWorkflowId(definition.getId());
		} else {
			if (Arrays.equals(attachment.getData(),
					workflow.getLatestWorkflowHistory().getAttachment().getData())) {
				LOG.info("New definition content is the same as old one");

				return workflow;
			}
		}

		WorkflowHistory newHistory = instanceFactory.getBean(WorkflowHistory.class.getSimpleName());

		newHistory.setAttachment(attachment);
		newHistory.setProcessDefinition(definition);

		workflow.addHistory(newHistory);

		definitionCache.put(workflow.getWorkflowId(), definition);
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
}
