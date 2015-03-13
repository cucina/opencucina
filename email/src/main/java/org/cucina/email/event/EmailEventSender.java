
package org.cucina.email.event;


/**
 * JAVADOC.
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
public interface EmailEventSender {
    /**
     * JAVADOC.
     *
     * @param event JAVADOC.
     */
    void processEvent(EmailEvent event);
}
