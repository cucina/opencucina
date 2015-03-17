package org.cucina.engine.server.event;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class CompensateEvent
    extends EngineEvent {
    private static final long serialVersionUID = 3984488467230882388L;
    private String type;
    private Long[] ids;

    /**
    * Creates a new CompensateEvent object.
    *
    * @param source JAVADOC.
    */
    public CompensateEvent(Object source) {
        super(source);
    }

    /**
     * Creates a new CompensateEvent object.
     */
    public CompensateEvent() {
    }

    /**
     * JAVADOC Method Level Comments
     */
    public void setIds(Long... ids) {
        this.ids = ids;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public Long[] getIds() {
        return ids;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param type JAVADOC.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public String getType() {
        return type;
    }
}
