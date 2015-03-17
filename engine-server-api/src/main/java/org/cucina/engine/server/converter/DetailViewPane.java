
package org.cucina.engine.server.converter;

import java.io.Serializable;
import java.util.List;


/**
 * Represents the properties required to make a DetailViewPane
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class DetailViewPane
    implements Serializable {
    private static final long serialVersionUID = -1831526921262197683L;
    private List<String> properties;
    private String title;

    /**
     * Creates a new DetailViewPane object.
     */
    public DetailViewPane() {
        super();
    }

    /**
    * Convenience constructor for DetailViewPane object.
    *
    * @param title String.
    * @param properties String.
    */
    public DetailViewPane(String title, List<String> properties) {
        super();
        this.title = title;
        this.properties = properties;
    }

    /**
     * Set properties
     *
     * @param properties List<String>.
     */
    public void setProperties(List<String> properties) {
        this.properties = properties;
    }

    /**
     * Get properties
     *
     * @return properties List<String>.
     */
    public List<String> getProperties() {
        return properties;
    }

    /**
     * Set title
     *
     * @param title String.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * The title of the pane
     *
     * @return title String.
     */
    public String getTitle() {
        return title;
    }
}
