package org.cucina.conversation.events;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class RollbackEvent
		extends OriginatedEngineEvent {
	private static final long serialVersionUID = -3679643248060887457L;
	private Object cause;

	/**
	 * Creates a new RollbackEvent object.
	 *
	 * @param source JAVADOC.
	 */
	public RollbackEvent(Object source) {
		super(source);
		this.cause = source;
	}

	/**
	 * Creates a new RollbackEvent object. Needed for JSON serializer
	 */
	public RollbackEvent() {
		super("JSON");
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	public Object getCause() {
		return cause;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param cause JAVADOC.
	 */
	public void setCause(Object cause) {
		this.cause = cause;
	}
}
