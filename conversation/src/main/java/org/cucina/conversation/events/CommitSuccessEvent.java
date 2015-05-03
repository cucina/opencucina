
package org.cucina.conversation.events;


/**
 * The result of a remote commit
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class CommitSuccessEvent
    extends ConversationEvent {
    private static final long serialVersionUID = 4075348036845630410L;

    /**
     * Creates a new CommitResultEvent object.
     *
     * @param source JAVADOC.
     */
    public CommitSuccessEvent(Object source) {
        super(source);
    }

    /**
     * Creates a new CommitSuccessEvent object.
     */
    public CommitSuccessEvent() {
        super();
    }
}
