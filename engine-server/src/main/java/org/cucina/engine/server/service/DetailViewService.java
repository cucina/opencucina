
package org.cucina.engine.server.service;

import java.util.Map;

import org.cucina.engine.server.converter.DetailViewConfig;
import org.cucina.engine.server.model.EntityDescriptor;


/**
 * Contains methods related to the display of data.
 *
 * @author $Author: $
 * @version $Revision: $
  */
public interface DetailViewService {

	/**
     * Get the data to display as values keyed by property name
     *
     * @param entityDescriptor EntityDescriptor.
     *
     * @return entityDescriptor EntityDescriptor.
     */
    Map<String, Object> getData(EntityDescriptor entityDescriptor);

    /**
     * Get the data to display as values keyed by property name
     *
     * @param id Long.
     * @param type String.
     * @param applicationName String.
     *
     * @return Map<String, Object>.
     */
    Map<String, Object> getData(Long id, String type, String applicationName);

    /**
     * Get detail view configuration.
     *
     * @param type String.
     * @param applicationName String.
     *
     * @return DetailViewConfig.
     */
    DetailViewConfig getDetailViewConfig(String type, String applicationName);
}
