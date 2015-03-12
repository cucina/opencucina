package org.cucina.engine.definition.config.xml;

import java.io.ByteArrayInputStream;
import java.io.StringReader;

import org.apache.commons.digester3.Digester;
import org.apache.commons.digester3.binder.DigesterLoader;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;

import org.cucina.engine.definition.StartStation;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class DigesterRulesModuleTest {
    private static final String SIMPLE_RULES = "<?xml version=\"1.0\"?>" +
        "<!DOCTYPE digester-rules PUBLIC " +
        " \"-//Apache Commons //DTD digester-rules XML V1.0//EN\" " +
        " \"http://commons.apache.org/digester/dtds/digester-rules-3.0.dtd\">" +
        "<digester-rules>" + "  <pattern value=\"start\">" +
        "    <object-create-rule classname=\"org.cucina.engine.definition.StartStation\" />" +
        "    <set-next-rule methodname=\"setStartState\" />" + "  </pattern>" +
        "</digester-rules>";
    private static final String SIMPLE_DATA = "<start/>";

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Test
    public void testLoadRules()
        throws Exception {
        Resource rulesResource = new InputStreamResource(new ByteArrayInputStream(
                    SIMPLE_RULES.getBytes()));
        DigesterRulesModule module = new DigesterRulesModule(rulesResource);
        DigesterLoader loader = DigesterLoader.newLoader(module);
        Digester digester = loader.newDigester();

        digester.push(new Container());

        Container cont = digester.parse(new StringReader(SIMPLE_DATA));

        assertNotNull("Container is null", cont);
        assertNotNull("startState is null", cont.getStartState());
    }

    /**
     * JAVADOC for Class Level
     *
     * @author $Author: $
     * @version $Revision: $
      */
    public class Container {
        private StartStation startState;

        /**
         * JAVADOC Method Level Comments
         *
         * @param startState JAVADOC.
         */
        public void setStartState(StartStation startState) {
            this.startState = startState;
        }

        /**
         * JAVADOC Method Level Comments
         *
         * @return JAVADOC.
         */
        public StartStation getStartState() {
            return startState;
        }
    }
}
