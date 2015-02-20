
package org.cucina.security.authentication;

import org.junit.Before;
import org.junit.Test;


/**
 * Test that checks nothing barfs
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class NoopUserDetailsCheckerTest {
    private NoopUserDetailsChecker checker;

    /**
     * Doesn't barf
     */
    @Test
    public void noop() {
        checker.check(null);
    }

    /**
     * sets up test
     */
    @Before
    public void onsetup() {
        checker = new NoopUserDetailsChecker();
    }
}
