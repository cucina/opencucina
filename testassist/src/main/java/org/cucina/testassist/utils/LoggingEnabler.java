
package org.cucina.testassist.utils;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


/**
 * Utility class enabling all logging for the given class. Helps to improve test coverage for
 * those conditional debug statements
 *
 * @author $author$
 * @version $Revision: 1.4 $
 */
public class LoggingEnabler {
    /**
     * JAVADOC Method Level Comments
     *
     * @param logname JAVADOC.
     */
    public static void disableLog(Class<?> clazz) {
        Logger logger = LogManager.getLogger(clazz);

        logger.setLevel(Level.ERROR);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param logname JAVADOC.
     */
    public static void disableLog(String logname) {
        Logger logger = LogManager.getLogger(logname);

        logger.setLevel(Level.ERROR);
    }

    /**
     * JAVADOC.
     *
     * @param clazz JAVADOC.
     */
    public static void enableLog(Class<?> clazz) {
        Logger logger = LogManager.getLogger(clazz);

        logger.setLevel(Level.DEBUG);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param logname JAVADOC.
     */
    public static void enableLog(String logname) {
        Logger logger = LogManager.getLogger(logname);

        logger.setLevel(Level.DEBUG);
    }
}
