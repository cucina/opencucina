
package org.cucina.cluster.event;

/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class SendBroadcastClusterEvent
    extends SendClusterEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4985813100093233448L;

	/**
     * Creates a new SendBroadcastClusterEvent object.
     *
     * @param source JAVADOC.
     */
    public SendBroadcastClusterEvent(String source) {
        super(source);

    }
}
