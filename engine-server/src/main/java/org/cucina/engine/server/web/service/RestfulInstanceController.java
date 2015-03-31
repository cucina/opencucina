package org.cucina.engine.server.web.service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import org.cucina.engine.model.ProcessToken;
import org.cucina.engine.server.repository.ProcessTokenRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * To list and view existing instances of workflows.
 *
 * @author $Author: $
 * @version $Revision: $
 */
@Controller
@RequestMapping(value = "/workflowInstance")
public class RestfulInstanceController {
    private static final Logger LOG = LoggerFactory.getLogger(RestfulInstanceController.class);
    private static final String[] SUMMARY_COLUMNS = {
            "workflowDefinitionId", "placeId", "domainObjectType", "domainObjectId",
            "domainObject.applicationName"
        };
    private ProcessTokenRepository processTokenRepository;

    /**
     * Creates a new WorkflowViewService object.
     *
     * @param workflowRepository
     *            JAVADOC.
     */
    @Autowired
    public RestfulInstanceController(ProcessTokenRepository entityDescriptorRepository) {
        Assert.notNull(entityDescriptorRepository, "entityDescriptorRepository is null");
        this.processTokenRepository = entityDescriptorRepository;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Collection<Object[]> listWorkflows() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("listWorkflows called");
        }

        return processTokenRepository.countByGroupProcessDefinitionId();
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param wfid
     *
     *
     * @return JAVADOC.
     */
    @RequestMapping(value = "/{wfid}", method = RequestMethod.GET)
    @ResponseBody
    public Collection<Object[]> workflowSummary(@PathVariable
    String wfid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("workflowSummary called with " + wfid);
        }

        Collection<ProcessToken> tokens = processTokenRepository.findByProcessDefinitionId(wfid);
        Collection<Object[]> result = new ArrayList<Object[]>();

        for (ProcessToken workflowToken : tokens) {
            BeanWrapper beanWrapper = new BeanWrapperImpl(workflowToken);
            Object[] line = new Object[SUMMARY_COLUMNS.length];

            for (int i = 0; i < SUMMARY_COLUMNS.length; i++) {
                line[i] = beanWrapper.getPropertyValue(SUMMARY_COLUMNS[i]);
            }

            result.add(line);
        }

        return result;
    }
}
