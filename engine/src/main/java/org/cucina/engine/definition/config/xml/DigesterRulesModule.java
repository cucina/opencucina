
package org.cucina.engine.definition.config.xml;

import java.io.IOException;

import org.apache.commons.digester3.xmlrules.FromXmlRulesModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class DigesterRulesModule
    extends FromXmlRulesModule {
    private static final Logger LOG = LoggerFactory.getLogger(DigesterRulesModule.class);
    private Resource rulesResource;

    /**
     * Creates a new DigesterRulesModule object.
     *
     * @param rulesResource JAVADOC.
     */
    public DigesterRulesModule(Resource rulesResource) {
        this.rulesResource = rulesResource;
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Override
    protected void loadRules() {
        try {
            loadXMLRules(rulesResource.getInputStream());
        } catch (IOException e) {
            LOG.error("Error loading rules", e);
            throw new IllegalArgumentException("Failed to load rules");
        }
    }
}
