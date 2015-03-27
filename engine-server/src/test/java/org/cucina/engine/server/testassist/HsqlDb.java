package org.cucina.engine.server.testassist;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang3.ArrayUtils;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.EntityManagerHolder;
import org.springframework.orm.jpa.JpaDialect;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import static org.junit.Assert.fail;


/**
 * Base test class that setup data access for an instance of a hypersonic db.
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class HsqlDb {
    private static HsqlDb hsqlDb;
    private BasicDataSource dataSource;

    /**
     * DataSource and Session Factory static so that we only create one Hypersonic db
     * accross all system tests
     */
    private EntityManagerFactory entityManagerFactory;
    private JdbcTemplate jdbcTemplate;

    /** TransactionManager for this test. */
    private JpaTransactionManager transactionManager;

    //Set once this has been initialized
    private boolean initialized;

    private HsqlDb() {
        super();
    }

    /**
     * Returns the EntityManager associated with the current transaction.
     *
     * @return
     */
    public EntityManager getEntityManager() {
        EntityManagerHolder holder = (EntityManagerHolder) TransactionSynchronizationManager.getResource(entityManagerFactory);

        return holder.getEntityManager();
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public static synchronized HsqlDb getInstance() {
        if (hsqlDb == null) {
            hsqlDb = new HsqlDb();
        }

        return hsqlDb;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public boolean isInitialized() {
        return initialized;
    }

    /**
    * JAVADOC Method Level Comments
    *
    * @return JAVADOC.
    */
    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public PlatformTransactionManager getTransactionManager() {
        return transactionManager;
    }

    /**
     * Initializes the hypersonic db.
     */
    public synchronized void init(String... packagesToScan) {
        if (ArrayUtils.isEmpty(packagesToScan)) {
            throw new RuntimeException("Package names to scan must be provided as arg");
        }

        if (isInitialized()) {
            //If already initialized just return cleanly
            return;
        }

        try {
            dataSource = new BasicDataSource();
            dataSource.setDriverClassName("org.hsqldb.jdbcDriver");
            dataSource.setUrl("jdbc:hsqldb:mem:sprite");
            dataSource.setUsername("sa");
            dataSource.setPassword("");

            JpaDialect dialect = new HibernateJpaDialect();
            LocalContainerEntityManagerFactoryBean localEMFactory = new LocalContainerEntityManagerFactoryBean();

            localEMFactory.setJpaDialect(dialect);
            localEMFactory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
            localEMFactory.setDataSource(dataSource);
            localEMFactory.setPackagesToScan(packagesToScan);

            //localEMFactory.setLoadTimeWeaver(new InstrumentationLoadTimeWeaver());
            Map<String, Object> properties = new HashMap<String, Object>();

            properties.put("eclipselink.logging.level", "FINE");
            properties.put("eclipselink.logging.level.sql", "FINE");
            properties.put("hibernate.hbm2ddl.auto", "create-drop");
            properties.put("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
            properties.put("PersistenceVersion", "2.1");
            localEMFactory.setJpaPropertyMap(properties);

            localEMFactory.afterPropertiesSet();

            entityManagerFactory = localEMFactory.getObject();
            jdbcTemplate = new JdbcTemplate(dataSource);

            transactionManager = new JpaTransactionManager();
            transactionManager.setJpaDialect(dialect);
            transactionManager.setEntityManagerFactory(entityManagerFactory);
            transactionManager.setDataSource(dataSource);
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Failed to initialize entityManager: " + ex);
        }

        initialized = true;
    }
}
