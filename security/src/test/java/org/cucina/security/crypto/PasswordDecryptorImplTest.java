
package org.cucina.security.crypto;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class PasswordDecryptorImplTest {
    @Mock
    private Encryptor encryptor;

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Before
    public void setUp()
        throws Exception {
        MockitoAnnotations.initMocks(this);
        when(encryptor.decrypt("password")).thenReturn("noproblem");
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testDecrypt() {
        PasswordDecryptorImpl decryptor = new PasswordDecryptorImpl(encryptor);

        decryptor.setCrypt(false);
        assertEquals("password", decryptor.decrypt("password"));
        decryptor.setCrypt(true);
        assertEquals("noproblem", decryptor.decrypt("password"));
    }
}
