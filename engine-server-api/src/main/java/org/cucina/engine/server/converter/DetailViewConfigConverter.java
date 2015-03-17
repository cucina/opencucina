package org.cucina.engine.server.converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import org.springframework.core.convert.converter.Converter;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Converts configured List of configuration items in to List of DetailViewConfig. If we move to
 * a schema based approach this could be ported across.
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class DetailViewConfigConverter
    implements Converter<Collection<Object>, Collection<DetailViewConfig>> {
    /** This is a field JAVADOC */
    public static final String TITLE_KEY = "title";

    /** This is a field JAVADOC */
    public static final String PROPERTIES_KEY = "properties";
    private static final Logger LOG = LoggerFactory.getLogger(DetailViewConfigConverter.class);

    /**
     * Convert to List of DetailViewConfig items
     *
     * @param map List<Map<String, Object>>>.
     *
     * @return List<DetailViewConfig>.
     */
    @Override
    public List<DetailViewConfig> convert(Collection<Object> input) {
        if (CollectionUtils.isEmpty(input)) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Empty input, return null");
            }

            return null;
        }

        List<DetailViewConfig> configs = new ArrayList<DetailViewConfig>();

        for (Object ed : input) {
            //String type = ed.getApplicationType();

            /*            List<String> properties = new ArrayList();


                        List<Map<String, Object>> panes = config.getValue();

                        Assert.isTrue(CollectionUtils.isNotEmpty(panes),
                            "Must provided list of pane values for type [" + type + "]");

                        List<DetailViewPane> detailViewPanes = new ArrayList<DetailViewPane>();

                        for (Map<String, Object> pane : panes) {
                            String title = (String) pane.get(TITLE_KEY);
                            @SuppressWarnings("unchecked")
                            List<String> properties = (List<String>) pane.get(PROPERTIES_KEY);

                            Assert.notNull(title,
                                "a title should be provided for each pane for type [" + type + "]");
                            Assert.isTrue(CollectionUtils.isNotEmpty(properties),
                                "Must provided list of properties values for each pane for type [" + type +
                                "]");

                            detailViewPanes.add(new DetailViewPane(title, properties));
                        }

                        configs.add(new DetailViewConfig(type, detailViewPanes));
            */
        }

        return configs;
    }
}
