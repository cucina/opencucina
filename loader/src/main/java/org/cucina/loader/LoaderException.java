
package org.cucina.loader;

/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class LoaderException
    extends Exception {
    /**
     *
     */
    private static final long serialVersionUID = 1535057442035747249L;
    private String[] errors;

    /**
     * Creates a new LoaderException object.
     *
     * @param errors JAVADOC.
     */
    public LoaderException(String[] errors) {
        super();
        this.errors = errors;
    }

    /**
     * Creates a new LoaderException object.
     *
     * @param message JAVADOC.
     * @param errors JAVADOC.
     */
    public LoaderException(String message, String[] errors) {
        super(message);
        this.errors = errors;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public String[] getErrors() {
        return errors;
    }
}
