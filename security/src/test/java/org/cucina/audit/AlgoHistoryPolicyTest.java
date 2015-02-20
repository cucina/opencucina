
package org.cucina.audit;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.cucina.core.service.ContextService;
import org.cucina.core.spring.SingletonBeanFactory;
import org.cucina.security.model.User;
import org.eclipse.persistence.internal.sessions.AbstractRecord;
import org.eclipse.persistence.internal.sessions.AbstractSession;
import org.eclipse.persistence.queries.DataModifyQuery;
import org.eclipse.persistence.queries.ObjectLevelModifyQuery;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.transaction.support.TransactionSynchronizationManager;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class AlgoHistoryPolicyTest {
    @Mock
    private BeanFactory beanFactory;
    @Mock
    private ContextService contextService;
    private User user;

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Before
    public void setUp()
        throws Exception {
        MockitoAnnotations.initMocks(this);
        when(beanFactory.getBean(SingletonBeanFactory.CONTEXT_SERVICE_ID))
            .thenReturn(contextService);

        ((SingletonBeanFactory) SingletonBeanFactory.getInstance()).setBeanFactory(beanFactory);

        SecurityContext context;

        if (null == SecurityContextHolder.getContext()) {
            context = new SecurityContextImpl();

            SecurityContextHolder.setContext(context);
        }

        context = SecurityContextHolder.getContext();

        user = new User();

        user.setId(new Long(1));
        user.setUsername("admin");

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user,
                null, null);

        context.setAuthentication(authToken);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testGetAuditUserField() {
        CucinaHistoryPolicy policy = new CucinaHistoryPolicy("table");

        assertEquals(CucinaHistoryPolicy.AUDIT_USER_FIELD, policy.getAuditUserField().getName());
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testGetCurrentTimeAbstractSession() {
        CucinaHistoryPolicy policy = new CucinaHistoryPolicy("table");

        policy.setShouldUseDatabaseTime(false);

        AbstractSession session = mock(AbstractSession.class);

        TransactionSynchronizationManager.initSynchronization();

        Object value = policy.getCurrentTime(session);

        verify(contextService).put("audit-time", value);
    }

    /**
     * JAVADOC Method Level Comments
     */
    public void testLogicalInsertObjectLevelModifyQueryBoolean() {
        CucinaHistoryPolicy policy = new CucinaHistoryPolicy("table");
        ObjectLevelModifyQuery writeQuery = mock(ObjectLevelModifyQuery.class);
        AbstractRecord modRow = mock(AbstractRecord.class);

        when(writeQuery.getModifyRow()).thenReturn(modRow);

        policy.logicalInsert(writeQuery, true);

        verify(modRow).add(policy.getAuditUserField(), user);
    }

    /**
     * JAVADOC Method Level Comments
     */
    public void testMappingLogicalInsertDataModifyQueryAbstractRecordAbstractSession() {
        CucinaHistoryPolicy policy = new CucinaHistoryPolicy("table");
        DataModifyQuery originalQuery = mock(DataModifyQuery.class);
        AbstractRecord modRow = mock(AbstractRecord.class);

        when(originalQuery.getModifyRow()).thenReturn(modRow);

        AbstractRecord arguments = mock(AbstractRecord.class);
        AbstractSession session = mock(AbstractSession.class);

        policy.mappingLogicalInsert(originalQuery, arguments, session);
    }
}
