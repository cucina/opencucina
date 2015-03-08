
package org.cucina.i18n;


/**
 * Provides locale an message text information.
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public interface I18nMessage {
    /**
     * JAVADOC.
     *
     * @return JAVADOC.
     */
    String getLocaleCd();

    /**
     * JAVADOC Method Level Comments
     */
    void setMessageTx(String messageTx);

    /**
     * JAVADOC.
     *
     * @return JAVADOC.
     */
    String getMessageTx();
}
