package org.cucina.i18n.model;


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
	 * JAVADOC.
	 *
	 * @return JAVADOC.
	 */
	String getMessageTx();

	/**
	 * JAVADOC Method Level Comments
	 */
	void setMessageTx(String messageTx);
}
