
package org.cucina.loader.agent;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public abstract class AbstractRouter
    implements Router {
    private Agent alternative;

    /**
     * Creates a new AbstractRouter object.
     *
     * @param alternative JAVADOC.
     */
    public AbstractRouter() {
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    public boolean route() {
        return doRoute();
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Override
    public void execute() {
        //no-op
    }

    /**
    * JAVADOC Method Level Comments
    */
    @Override
    public void runAlternative() {
        if (alternative != null) {
            alternative.execute();
        }
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    protected abstract boolean doRoute();

    /**
     * JAVADOC Method Level Comments
     *
     * @param alternative JAVADOC.
     */
    public void setAlternative(Agent alternative) {
        this.alternative = alternative;
    }
}
