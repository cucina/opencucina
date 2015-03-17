package org.cucina.engine.server.web.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.cucina.engine.server.converter.DetailViewConfig;
import org.cucina.engine.server.converter.DetailViewPane;
import org.cucina.engine.server.service.DetailViewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
@Controller
public class DetailViewWebService {
    private static final Logger LOG = LoggerFactory.getLogger(DetailViewWebService.class);
    private DetailViewService detailViewService;

    /**
     * Creates a new DetailViewService object.
     *
     * @param dataViewService DetailViewService.
     */
    public DetailViewWebService(DetailViewService detailViewService) {
        super();
        Assert.notNull(detailViewService, "detailViewService must be provided as an argument");
        this.detailViewService = detailViewService;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param localId JAVADOC.
     *
     * @return JAVADOC.
     */
    @RequestMapping(value = "/domainConfig", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> readDomainConfig(
        @RequestParam(value = "applicationType", required = true)
    String type, @RequestParam(value = "applicationName", required = true)
    String applicationName) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Loading config for domain type [" + type + "]");
        }

        DetailViewConfig config = detailViewService.getDetailViewConfig(type, applicationName);

        if (config == null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("No config found for type [" + type + "]");
            }

            return null;
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Loaded up data view config for type [" + type + "], config [" + config +
                "]");
        }

        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

        for (DetailViewPane pane : config.getPanes()) {
            Map<String, Object> serPane = new HashMap<String, Object>();

            serPane.put("title", pane.getTitle());
            serPane.put("properties", pane.getProperties());
            result.add(serPane);
        }

        return result;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param localId JAVADOC.
     *
     * @return JAVADOC.
     */
    @RequestMapping(value = "/domainData", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> readDomainData(
        @RequestParam(value = "id", required = true)
    Long id, @RequestParam(value = "applicationType", required = true)
    String type, @RequestParam(value = "applicationName", required = true)
    String applicationName) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Loading domain data for type [" + type + "] with id [" + id +
                "] from application with name [" + applicationName + "]");
        }

        Map<String, Object> result = detailViewService.getData(id, type, applicationName);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Loaded up domain data for entity type [" + type + "] with id [" + id +
                "], name values [" + result + "]");
        }

        List<Map<String, Object>> serializedResults = new ArrayList<Map<String, Object>>();

        if (MapUtils.isNotEmpty(result)) {
            for (Map.Entry<String, Object> propertyItem : result.entrySet()) {
                Map<String, Object> data = new HashMap<String, Object>();

                data.put("name", propertyItem.getKey());
                data.put("value", propertyItem.getValue());
                serializedResults.add(data);
            }
        }

        return serializedResults;
    }
}
