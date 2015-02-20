package org.cucina.security.access;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.cucina.security.model.Privilege;
import org.cucina.security.repository.PrivilegeRepository;
import org.cucina.testassist.utils.LoggingEnabler;
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
public class AccessRegistryImplTest {
    private AccessRegistryImpl impl;
    @Mock
    private PrivilegeRepository privilegeRepository;

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Before
    public void setUp()
        throws Exception {
        LoggingEnabler.enableLog(AccessRegistryImpl.class);
        MockitoAnnotations.initMocks(this);
        impl = new AccessRegistryImpl(privilegeRepository);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testDefaults() {
        impl.setDefaultPrivilegeName(null);
        assertNull(impl.getDefaultPrivilege());
        impl.setSystemPrivilegeName(null);
        assertNull(impl.getSystemPrivilege());

        impl.setDefaultPrivilegeName("default");
        when(privilegeRepository.findByName("default")).thenReturn(new Privilege());
        assertNotNull(impl.getDefaultPrivilege());

        impl.setSystemPrivilegeName("system");
        when(privilegeRepository.findByName("system")).thenReturn(new Privilege());
        assertNotNull(impl.getSystemPrivilege());
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testLookup() {
        Map<String, String> strings = new HashMap<String, String>();
        String priv = "priv";
        String priv2 = "priv2";
        String defpriv = "default";
        Privilege privP = new Privilege();

        privP.setName(priv);

        Privilege privP2 = new Privilege();

        privP2.setName(priv2);

        Privilege defaultPriv = new Privilege();

        defaultPriv.setName(defpriv);

        strings.put("Blaah.view", "priv");
        strings.put("Blaah", "priv2");
        when(privilegeRepository.findByName(priv)).thenReturn(privP);
        when(privilegeRepository.findByName(priv2)).thenReturn(privP2);
        when(privilegeRepository.findByName(defpriv)).thenReturn(defaultPriv);

        impl.setTypeToPriv(strings);
        impl.setDefaultPrivilegeName(defpriv);
        assertEquals(privP, impl.lookup("Blaah", "view"));
        assertEquals(privP2, impl.lookup("Blaah", "anything"));
        assertEquals(defaultPriv, impl.lookup("XXX", "anything"));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testNoMap() {
        String defpriv = "default";

        Privilege defaultPriv = new Privilege();

        defaultPriv.setName(defpriv);
        when(privilegeRepository.findByName(defpriv)).thenReturn(defaultPriv);
        impl.setDefaultPrivilegeName(defpriv);

        try {
            impl.lookup("type", "access");
        } catch (Throwable t) {
            fail("shouldn't throw exception");
        }
    }
}
