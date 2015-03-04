package org.cucina.eggtimer;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class ResponseHandler {
    private Handler handler;

    /**
     * JAVADOC Method Level Comments
     *
     * @param harness JAVADOC.
     */
    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param message JAVADOC.
     */
    public void handleMessage(String message) {
        handler.handleResponse(message);
    }
}
