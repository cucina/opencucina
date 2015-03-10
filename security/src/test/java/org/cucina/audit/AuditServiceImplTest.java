package org.cucina.audit;

import java.sql.Timestamp;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import org.cucina.core.InstanceFactory;
import org.cucina.core.spring.SingletonBeanFactory;

import org.cucina.i18n.model.Message;
import org.cucina.i18n.model.MutableI18nMessage;
import org.cucina.i18n.repository.MessageRepository;

import org.cucina.security.testassist.Foo;

import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.eclipse.persistence.history.AsOfClause;
import org.eclipse.persistence.history.HistoryPolicy;
import org.eclipse.persistence.internal.sessions.DatabaseSessionImpl;
import org.eclipse.persistence.jpa.JpaEntityManager;
import org.eclipse.persistence.sessions.Project;
import org.eclipse.persistence.sessions.Session;
import org.eclipse.persistence.sessions.server.ClientSession;
import org.eclipse.persistence.sessions.server.ServerSession;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import org.mockito.ArgumentMatcher;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;

import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.MockitoAnnotations;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class AuditServiceImplTest {
    private static final String EXPECTED_QUERY = "select INF_START_DATE,INF_END_DATE,INF_AUDIT_USER from T_FOO where id = ? order by INF_START_DATE desc";
    private AuditServiceImpl auditService;
    @Mock
    private BeanFactory beanFactory;
    @Mock
    private InstanceFactory instanceFactory;
    @Mock
    private JdbcTemplate jdbcTemplate;
    @Mock
    private JpaEntityManager entityManager;
    @Mock
    private MessageRepository messageRepository;

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Before
    public void setUp()
        throws Exception {
        MockitoAnnotations.initMocks(this);
        auditService = new AuditServiceImpl();
        auditService.setInstanceFactory(instanceFactory);
        auditService.setJdbcTemplate(jdbcTemplate);
        auditService.setEntityManager(entityManager);
        when(instanceFactory.getBean(any(String.class))).thenReturn(new MutableI18nMessage())
            .thenReturn(new MutableI18nMessage());
        //when(messageDao.getDefaultLocale()).thenReturn(Locale.ENGLISH);
        when(beanFactory.getBean(SingletonBeanFactory.INSTANCE_FACTORY_ID))
            .thenReturn(instanceFactory);

        //   when(beanFactory.getBean(SingletonBeanFactory.I18N_SERVICE_ID)).thenReturn(i18nService);
        when(beanFactory.getBean(MessageRepository.MESSAGE_REPOSITORY_ID))
            .thenReturn(messageRepository);
        ((SingletonBeanFactory) SingletonBeanFactory.getInstance()).setBeanFactory(beanFactory);
    }

    /**
     * Test that we only attempt to find audit records for those
     * entities which are historized.
     */
    @Test
    public void testListAuditRecords() {
        when(instanceFactory.<Foo>getClassType(Foo.TYPE)).thenReturn(Foo.class);

        DatabaseSessionImpl session = mock(DatabaseSessionImpl.class);
        Project project = mock(Project.class);
        ClassDescriptor classDescriptor = mock(ClassDescriptor.class);
        HistoryPolicy hPolicy = new CucinaHistoryPolicy("FOO");

        when(entityManager.getDatabaseSession()).thenReturn(session);
        when(session.getProject()).thenReturn(project);
        when(project.getDescriptor(Foo.class)).thenReturn(classDescriptor);
        when(classDescriptor.getHistoryPolicy()).thenReturn(hPolicy);

        Long id = 1L;
        Map<String, Object> ar = new HashMap<String, Object>();

        ar.put(CucinaHistoryPolicy.INF_START_DATE, new Timestamp(System.currentTimeMillis()));
        ar.put(CucinaHistoryPolicy.INF_END_DATE, new Timestamp(System.currentTimeMillis()));

        Map<String, Object> ar1 = new HashMap<String, Object>();

        ar1.put(CucinaHistoryPolicy.INF_START_DATE, new Timestamp(System.currentTimeMillis()));

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> qResults = Arrays.asList(ar1, ar);

        when(jdbcTemplate.queryForList(EXPECTED_QUERY, id)).thenReturn(qResults);

        List<AuditRecord> results = auditService.listAuditRecords(Foo.TYPE, id);

        verify(instanceFactory).getClassType(Foo.TYPE);
        verify(entityManager).getDatabaseSession();
        verify(session).getProject();
        verify(project).getDescriptor(Foo.class);
        verify(classDescriptor).getHistoryPolicy();
        verify(jdbcTemplate).queryForList(EXPECTED_QUERY, id);

        assertEquals("Wrong number of audit records", 2, results.size());
        assertEquals("Should be INSERT", "UPDATE", results.get(0).getAction());
        assertEquals("Should be INSERT", "INSERT", results.get(1).getAction());
    }

    /**
     * Test that we only attempt to find audit records for those
     * entities which are historized.
     */
    @Test
    public void testListAuditRecordsForDeleted() {
        when(instanceFactory.<Foo>getClassType(Foo.TYPE)).thenReturn(Foo.class);

        DatabaseSessionImpl session = mock(DatabaseSessionImpl.class);
        Project project = mock(Project.class);
        ClassDescriptor classDescriptor = mock(ClassDescriptor.class);
        HistoryPolicy hPolicy = new CucinaHistoryPolicy("FOO");

        when(entityManager.getDatabaseSession()).thenReturn(session);
        when(session.getProject()).thenReturn(project);
        when(project.getDescriptor(Foo.class)).thenReturn(classDescriptor);
        when(classDescriptor.getHistoryPolicy()).thenReturn(hPolicy);

        Long id = 1L;
        Map<String, Object> ar = new HashMap<String, Object>();

        ar.put(CucinaHistoryPolicy.INF_START_DATE, new Timestamp(System.currentTimeMillis()));
        ar.put(CucinaHistoryPolicy.INF_END_DATE, new Timestamp(System.currentTimeMillis()));

        Map<String, Object> ar1 = new HashMap<String, Object>();

        ar1.put(CucinaHistoryPolicy.INF_START_DATE, new Timestamp(System.currentTimeMillis()));
        ar1.put(CucinaHistoryPolicy.INF_END_DATE, new Timestamp(System.currentTimeMillis()));

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> qResults = Arrays.asList(ar1, ar);

        when(jdbcTemplate.queryForList(EXPECTED_QUERY, id)).thenReturn(qResults);

        List<AuditRecord> results = auditService.listAuditRecords(Foo.TYPE, id);

        verify(instanceFactory).getClassType(Foo.TYPE);
        verify(entityManager).getDatabaseSession();
        verify(session).getProject();
        verify(project).getDescriptor(Foo.class);
        verify(classDescriptor).getHistoryPolicy();
        verify(jdbcTemplate).queryForList(EXPECTED_QUERY, id);

        assertEquals("Wrong number of audit records", 2, results.size());
        assertEquals("Should be INSERT", "DELETE", results.get(0).getAction());
        assertEquals("Should be INSERT", "INSERT", results.get(1).getAction());
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testListChangeDetails() {
        Foo newFoo = new Foo();
        Foo oldFoo = new Foo();
        Long id = 1L;

        newFoo.setId(id);
        oldFoo.setId(id);

        newFoo.setName("newFoo");
        oldFoo.setName("oldFoo");

        Calendar cal = Calendar.getInstance();

        newFoo.setDate(cal.getTime());
        cal.roll(Calendar.SECOND, false);
        oldFoo.setDate(cal.getTime());

        Message msg = new Message();

        msg.setId(id);
        msg.setMessageTx("hello", Locale.ENGLISH.toString());

        Message oldMsg = new Message();

        oldMsg.setId(id);
        oldMsg.setMessageTx("old hello", Locale.ENGLISH.toString());
        newFoo.setMessage(msg);
        oldFoo.setMessage(oldMsg);

        Timestamp auditDate = new Timestamp(Calendar.getInstance().getTimeInMillis());
        AsOfMatcher currMatcher = new AsOfMatcher(auditDate);
        AsOfMatcher prevMatcher = new AsOfMatcher(new Timestamp(auditDate.getTime() - 1));
        ServerSession serverSession = mock(ServerSession.class);
        ClientSession clientSession = mock(ClientSession.class);
        Session histSession = mock(Session.class);

        Foo readObj = new Foo();

        when(entityManager.getServerSession()).thenReturn(serverSession);
        when(serverSession.acquireClientSession()).thenReturn(clientSession);
        when(instanceFactory.getBean(Foo.TYPE)).thenReturn(readObj);
        when(clientSession.acquireHistoricalSession(argThat(currMatcher))).thenReturn(histSession);
        when(clientSession.acquireHistoricalSession(argThat(prevMatcher))).thenReturn(histSession);
        when(histSession.readObject(readObj)).thenReturn(newFoo).thenReturn(oldFoo);

        List<AuditDifference> diffs = auditService.listChangeDetails(Foo.TYPE, id, auditDate);

        assertEquals("wrong number of differences", 3, diffs.size());
        verify(serverSession).acquireClientSession();
        verify(clientSession).acquireHistoricalSession(argThat(currMatcher));
        verify(instanceFactory).getBean(Foo.TYPE);
        verify(clientSession).acquireHistoricalSession(argThat(prevMatcher));
        verify(histSession, times(2)).readObject(readObj);
    }

    private class AsOfMatcher
        extends ArgumentMatcher<AsOfClause> {
        private Date compareDate;

        public AsOfMatcher(Date compareDate) {
            this.compareDate = compareDate;
        }

        @Override
        public boolean matches(Object actual) {
            return (actual != null) && compareDate.equals(((AsOfClause) actual).getValue());
        }
    }
}
