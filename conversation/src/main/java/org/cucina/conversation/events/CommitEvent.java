package org.cucina.conversation.events;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class CommitEvent
		extends OriginatedEngineEvent {
	private static final long serialVersionUID = -3679643248060887457L;

	/**
	 * Creates a new CommitEvent object.
	 *
	 * @param source JAVADOC.
	 */
	public CommitEvent(Object source) {
		super(source);
	}

	/**
	 * Creates a new CommitEvent object.
	 */
	public CommitEvent() {
	}
}
