package org.cucina.audit;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Vector;

import org.cucina.security.testassist.Bar;
import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.eclipse.persistence.history.HistoryPolicy;
import org.eclipse.persistence.mappings.DatabaseMapping;
import org.eclipse.persistence.mappings.ManyToManyMapping;
import org.eclipse.persistence.sessions.Project;
import org.eclipse.persistence.sessions.Session;
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
public class HistorySessionCustomizerTest {
    @Mock
    private ClassDescriptor descriptor;
    @Mock
    private ClassDescriptor refDescriptor;
    @Mock
    private ManyToManyMapping mtm;
    @Mock
    private Project project;
    @Mock
    private Session session;

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Before
    public void setUp()
        throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Test
    public void testCustomize()
        throws Exception {
        HistorySessionCustomizer customizer = new HistorySessionCustomizer();

        when(session.getProject()).thenReturn(project);

        List<ClassDescriptor> descs = Collections.singletonList(descriptor);

        when(project.getOrderedDescriptors()).thenReturn(descs);
        when(descriptor.getHistoryPolicy()).thenReturn(new HistoryPolicy());

        Vector<DatabaseMapping> mappings = new Vector<DatabaseMapping>();

        mappings.add(mtm);
        when(descriptor.getMappings()).thenReturn(mappings);
        when(mtm.isManyToManyMapping()).thenReturn(true);

        Class<Bar> refClass = Bar.class;

        when(mtm.getReferenceClass()).thenReturn(refClass);
        when(session.getClassDescriptor(refClass)).thenReturn(refDescriptor);
        when(refDescriptor.getHistoryPolicy()).thenReturn(new HistoryPolicy());
        when(mtm.getHistoryPolicy()).thenReturn(null);
        when(mtm.getRelationTableName()).thenReturn("rtm");

        customizer.customize(session);

        verify(mtm).setHistoryPolicy(any(CucinaHistoryPolicy.class));
    }
}
