
package org.cucina.engine.definition.config.xml;

import org.cucina.engine.definition.Transition;


/**
 * Convenient wrapper for Transition to allow us to set the value of the
 * "to" property.
 */
public class WrapperTransition
    extends Transition {
    private static final long serialVersionUID = -943323560684135631L;
    private String to;

    /**
     * @param to
     *            The name of the new place.
     */
    public void setTo(String to) {
        this.to = to;
    }

    /**
     * @return Returns the to.
     */
    public String getTo() {
        return to;
    }
}
