package org.cucina.engine.server.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.cucina.engine.server.converter.DetailViewConfig;
import org.cucina.engine.server.converter.DetailViewPane;
import org.cucina.engine.server.model.EntityDescriptor;


/**
 * Mock for testing
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class LocalDetailViewService
    implements DetailViewService {
    /**
     * JAVADOC Method Level Comments
     *
     * @param entityDescriptor
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public Map<String, Object> getData(EntityDescriptor entityDescriptor) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param id
     *            JAVADOC.
     * @param type
     *            JAVADOC.
     * @param applicationName
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public Map<String, Object> getData(Long id, String type, String applicationName) {
        Map<String, Object> result = new HashMap<String, Object>();
        int top = RandomUtils.nextInt(1, 5);

        for (int i = 0; i < top; i++) {
            result.put("property" + RandomStringUtils.randomNumeric(3),
                RandomStringUtils.randomAlphabetic(15));
        }

        return result;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param type
     *            JAVADOC.
     * @param applicationName
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public DetailViewConfig getDetailViewConfig(String type, String applicationName) {
        List<DetailViewPane> panes = new ArrayList<DetailViewPane>();

        int top = RandomUtils.nextInt(1, 5);

        for (int i = 0; i < top; i++) {
            panes.add(buildPane());
        }

        DetailViewConfig config = new DetailViewConfig("type", panes);

        return config;
    }

    private DetailViewPane buildPane() {
        List<String> properties = new ArrayList<String>();

        int top = RandomUtils.nextInt(1, 10);

        for (int i = 0; i < top; i++) {
            properties.add("property " + RandomStringUtils.randomNumeric(3));
        }

        return new DetailViewPane("title " + RandomStringUtils.randomNumeric(4), properties);
    }
}
