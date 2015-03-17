
package org.cucina.engine.server.event;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public abstract class GetValueEvent
    extends OriginatedEngineEvent {
    private static final long serialVersionUID = 8820145100424527864L;

    /**
    * Creates a new GetValueEvent object.
    *
    * @param source JAVADOC.
    */
    public GetValueEvent(Object source, String applicationName) {
        super(source);
        setApplicationName(applicationName);
    }

    /**
     * Creates a new GetValueEvent object. Needs this for JSON.
     */
    public GetValueEvent() {
    }
}
