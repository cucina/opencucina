
package org.cucina.engine.listeners;

import org.junit.Test;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class WorkflowListenerAdaperTest {
    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void test() {
        WorkflowListenerAdapter adapter = new WorkflowListenerAdapter() {
            };

        adapter.enteredState(null, null, null);
        adapter.leavingState(null, null, null);
        adapter.startingSession(null);
    }
}
