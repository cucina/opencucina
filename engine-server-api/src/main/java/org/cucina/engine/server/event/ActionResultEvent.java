
package org.cucina.engine.server.event;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class ActionResultEvent
    extends EngineEvent {
    private static final long serialVersionUID = 7394957791642084513L;

    /**
     * Creates a new ActionResultEvent object.
     *
     * @param source JAVADOC.
     */
    public ActionResultEvent(Object source) {
        super(source);
    }

    /**
     * Creates a new ActionResultEvent object.
     */
    public ActionResultEvent() {
    }
}
