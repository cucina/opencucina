
package org.cucina.engine.server.converter;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;


/**
 * Represents the Detail View configuration for a particular type.
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class DetailViewConfig
    implements Serializable {
    private static final long serialVersionUID = 1948974704200089065L;
    private List<DetailViewPane> panes;
    private String type;

    /**
     * Creates a new DetailViewConfig object.
     */
    public DetailViewConfig() {
        super();
    }

    /**
    * Convenience constructor creating a new DetailViewConfig object.
    *
    * @param type String.
    * @param panes List<DetailViewPane>.
    */
    public DetailViewConfig(String type, List<DetailViewPane> panes) {
        super();
        this.type = type;
        this.panes = panes;
    }

    /**
     * Set panes
     *
     * @param panes List<DetailViewPane>.
     */
    public void setPanes(List<DetailViewPane> panes) {
        this.panes = panes;
    }

    /**
     * Get panes
     *
     * @return panes List<DetailViewPane>.
     */
    public List<DetailViewPane> getPanes() {
        return panes;
    }

    /**
     * Set type
     *
     * @param type String.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Get type
     *
     * @return type String.
     */
    public String getType() {
        return type;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
