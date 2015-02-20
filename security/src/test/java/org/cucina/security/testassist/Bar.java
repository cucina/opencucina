package org.cucina.security.testassist;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.cucina.core.model.PersistableEntity;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
@Entity(name = Bar.TYPE)
public class Bar
    extends PersistableEntity {
    /** Bar */
    public static final String TYPE = "Bar";
    private static final long serialVersionUID = 1L;
    private Baz baz;
    private Foo foo;
    private String barDescription;
    private String name;

    /**
         * Creates a new Bar object.
         */
    public Bar() {
        super();
    }

    /**
         * Creates a new Bar object.
         *
         * @param id
         *            JAVADOC.
         */
    public Bar(Long id) {
        super();
        setId(id);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param barDescription JAVADOC.
     */
    public void setBarDescription(String barDescription) {
        this.barDescription = barDescription;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public String getBarDescription() {
        return barDescription;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param baz JAVADOC.
     */
    public void setBaz(Baz baz) {
        this.baz = baz;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @ManyToOne
    public Baz getBaz() {
        return baz;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param foo JAVADOC.
     */
    public void setFoo(Foo foo) {
        this.foo = foo;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @ManyToOne
    public Foo getFoo() {
        return foo;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param name JAVADOC.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public String getName() {
        return name;
    }
}
