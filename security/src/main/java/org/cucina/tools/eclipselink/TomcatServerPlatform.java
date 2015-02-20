package org.cucina.tools.eclipselink;

import java.sql.Connection;
import java.sql.SQLException;

import org.eclipse.persistence.platform.server.ServerPlatformBase;
import org.eclipse.persistence.sessions.DatabaseSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.support.nativejdbc.CommonsDbcpNativeJdbcExtractor;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class TomcatServerPlatform
    extends ServerPlatformBase {
    private static final Logger LOG = LoggerFactory.getLogger(TomcatServerPlatform.class);

    /**
     * Creates a new TomcatServerPlatform object.
     *
     * @param newDatabaseSession JAVADOC.
     */
    public TomcatServerPlatform(DatabaseSession newDatabaseSession) {
        super(newDatabaseSession);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @SuppressWarnings("rawtypes")
    @Override
    public Class getExternalTransactionControllerClass() {
        return null;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param connection JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public Connection unwrapConnection(Connection connection) {
        CommonsDbcpNativeJdbcExtractor nje = new CommonsDbcpNativeJdbcExtractor();

        try {
            connection = nje.getNativeConnection(connection);
        } catch (SQLException e) {
            LOG.error("Oops", e);
        }

        return connection;
    }
}
