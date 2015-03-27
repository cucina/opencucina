package org.cucina.i18n.testassist;

import javax.persistence.EntityManager;

import org.apache.commons.lang3.ClassUtils;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import org.cucina.core.CompositeInstanceFactory;
import org.cucina.core.InstanceFactory;
import org.cucina.core.PackageBasedInstanceFactory;
import org.cucina.core.service.ContextService;
import org.cucina.core.service.ThreadLocalContextService;
import org.cucina.core.spring.SingletonBeanFactory;

import org.cucina.i18n.model.Message;

import org.junit.After;
import org.junit.Before;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * Base test class that setup data access for an instance of a hypersonic db.
 *
 * @author $Author: $
 * @version $Revision: $
 */
public abstract class JpaProvider {
    private BeanFactory bfMock;
    private InstanceFactory instanceFactory;

    /**
     * TransactionStatus for this test. Typical subclasses won't need to use it.
     */
    private TransactionStatus transactionStatus;

    /** Number of transactions started */
    private int transactionsStarted;

    /**
     * Creates a new JpaProvider object.
     */
    public JpaProvider() {
        init(ClassUtils.getPackageName(Foo.class), ClassUtils.getPackageName(Message.class));
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public BeanFactory getBfMock() {
        return bfMock;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public EntityManager getEntityManager() {
        return HsqlDb.getInstance().getEntityManager();
    }

    /**
     * Returns <code>InstanceFactory</code> to be used in child classes.
     *
     * @return instanceFactory
     */
    public InstanceFactory getInstanceFactory() {
        return instanceFactory;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public JdbcTemplate getJdbcTemplate() {
        return HsqlDb.getInstance().getJdbcTemplate();
    }

    /**
     * @return
     */
    public PlatformTransactionManager getTransactionManager() {
        return HsqlDb.getInstance().getTransactionManager();
    }

    /**
     * This implementation creates a transaction before test execution. Override
     * <code>onSetUpBeforeTransaction</code> and/or
     * <code>onSetUpInTransaction</code> to add custom set-up behavior.
     *
     * @see #onSetUpBeforeTransaction()
     * @see #onSetUpInTransaction()
     */
    @Before
    public final void setUp()
        throws Exception {
        onSetUpBeforeTransaction();

        if (HsqlDb.getInstance().getTransactionManager() != null) {
            startNewTransaction();
        } else {
            System.out.println("HsqlJpaProvider:" +
                "No transaction manager set: tests will NOT run within a transaction");
        }

        onSetUp();
    }

    /**
     * This implementation rolls back the transaction after test execution.
     *
     * @throws Exception
     *             simply let any exception propagate
     */
    @After
    public final void tearDown()
        throws Exception {
        try {
            onTearDown();
        } finally {
            rollBackTransaction();
        }

        onTearDownAfterTransaction();
    }

    /**
     * JAVADOC.
     */
    protected void commitTransaction() {
        if (this.transactionStatus != null) {
            try {
                HsqlDb.getInstance().getTransactionManager().commit(this.transactionStatus);
                System.out.println("HsqlJpaProvider:" +
                    "Committed transaction after test execution");
            } finally {
                this.transactionStatus = null;
            }
        }
    }

    /**
     * Initializes the infrastructure classes that are required for data access.
     */
    protected void init(String... modelPackageNames) {
        HsqlDb.getInstance().init(modelPackageNames);

        CompositeInstanceFactory cif = new CompositeInstanceFactory();

        for (int i = 0; i < modelPackageNames.length; i++) {
            cif.getInstanceFactories().add(new PackageBasedInstanceFactory(modelPackageNames[i]));
        }

        this.instanceFactory = cif;

        final ContextService contextService = new ThreadLocalContextService();

        bfMock = mock(BeanFactory.class);

        when(bfMock.getBean("contextService")).thenReturn(contextService);
        ((SingletonBeanFactory) SingletonBeanFactory.getInstance()).setBeanFactory(bfMock);
    }

    /**
     * Subclasses can override this method to perform any setup operations,
     * within the transaction
     *
     * @throws Exception
     *             simply let any exception propagate
     */
    protected void onSetUp()
        throws Exception {
    }

    /**
     * Subclasses can override this method to perform any setup operations, such
     * as populating a database table, <i>before</i> the transaction created by
     * this class.
     *
     * @throws Exception
     *             simply let any exception propagate
     */
    protected void onSetUpBeforeTransaction()
        throws Exception {
    }

    /**
     * User can override to clear up any resources before the transaction is
     * finished
     */
    protected void onTearDown()
        throws Exception {
    }

    /**
     * User can override to perform operations after the transaction has
     * finished
     */
    protected void onTearDownAfterTransaction()
        throws Exception {
    }

    /**
     * JAVADOC.
     */
    protected void rollBackTransaction() {
        if (this.transactionStatus != null) {
            try {
                HsqlDb.getInstance().getTransactionManager().rollback(this.transactionStatus);
                System.out.println("HsqlJpaProvider:" +
                    "Rolled back transaction after test execution");
            } finally {
                this.transactionStatus = null;
            }
        }
    }

    /**
     * Starts a new transaction if necessary. The fate of the new transaction,
     * by default, will be the usual rollback.
     *
     * @see #endTransaction()
     * @see #setComplete()
     */
    protected void startNewTransaction()
        throws TransactionException {
        if (this.transactionStatus != null) {
            return;
        }

        this.transactionStatus = HsqlDb.getInstance().getTransactionManager()
                                       .getTransaction(new DefaultTransactionDefinition());
        ++this.transactionsStarted;

        System.out.println("HsqlJpaProvider:" + "Began transaction (" + this.transactionsStarted +
            "): transaction manager [" + HsqlDb.getInstance().getTransactionManager() + "];");
    }
}
