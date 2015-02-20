package org.cucina.security.bean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Collection;

import org.cucina.security.model.Privilege;
import org.cucina.security.repository.PrivilegeRepository;
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
public class PrivilegesEditorTest {
    private static final String PRIVILEGE1_NAME = "privilege1";
    private static final String PRIVILEGE2_NAME = "privilege2";
    private Privilege privilege1;
    private Privilege privilege2;
    @Mock
    private PrivilegeRepository privilegeRepository;
    private PrivilegesEditor editor;

    /**
     * If empty text is provided does nothing
     */
    @Test
    public void emptyText() {
        editor.setAsText("");

        @SuppressWarnings("unchecked")
        Collection<Privilege> privileges = (Collection<Privilege>) editor.getValue();

        assertNotNull("Should return collection of privileges", privileges);
        assertEquals("Incorrect number privileges", 0, privileges.size());
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void loadsPrivileges() {
        when(privilegeRepository.findByNameForUpdate(PRIVILEGE1_NAME)).thenReturn(privilege1);
        when(privilegeRepository.findByNameForUpdate(PRIVILEGE2_NAME)).thenReturn(privilege2);

        editor.setAsText(PRIVILEGE1_NAME + "," + PRIVILEGE2_NAME);

        @SuppressWarnings("unchecked")
        Collection<Privilege> privileges = (Collection<Privilege>) editor.getValue();

        assertNotNull("Should return collection of privileges", privileges);
        assertEquals("Incorrect number privileges", 2, privileges.size());
        assertTrue("Should contain privilege1", privileges.contains(privilege1));
        assertTrue("Should contain privilege2", privileges.contains(privilege2));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void loadsPrivilegesWithWhitespace() {
        when(privilegeRepository.findByNameForUpdate(PRIVILEGE1_NAME)).thenReturn(privilege1);
        when(privilegeRepository.findByNameForUpdate(PRIVILEGE2_NAME)).thenReturn(privilege2);

        editor.setAsText(PRIVILEGE1_NAME + " , " + PRIVILEGE2_NAME);

        @SuppressWarnings("unchecked")
        Collection<Privilege> privileges = (Collection<Privilege>) editor.getValue();

        assertNotNull("Should return collection of privileges", privileges);
        assertEquals("Incorrect number privileges", 2, privileges.size());
        assertTrue("Should contain privilege1", privileges.contains(privilege1));
        assertTrue("Should contain privilege2", privileges.contains(privilege2));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Before
    public void onsetup() {
        MockitoAnnotations.initMocks(this);
        editor = new PrivilegesEditor(privilegeRepository);
        privilege1 = new Privilege();
        privilege1.setId(11L);
        privilege1.setName(PRIVILEGE1_NAME);

        privilege2 = new Privilege();
        privilege2.setId(12L);
        privilege2.setName(PRIVILEGE2_NAME);
    }

    /**
     * If
     */
    @Test(expected = IllegalArgumentException.class)
    public void privilegeNoLongerExists() {
        when(privilegeRepository.findByNameForUpdate(PRIVILEGE1_NAME)).thenReturn(null);

        editor.setAsText(PRIVILEGE1_NAME + "," + PRIVILEGE2_NAME);
    }
}
