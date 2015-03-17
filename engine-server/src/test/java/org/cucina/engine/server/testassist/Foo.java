package org.cucina.engine.server.testassist;

import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.cucina.core.model.PersistableEntity;
import org.cucina.core.model.Versioned;
import org.cucina.core.model.projection.ProjectionColumn;
import org.cucina.core.model.projection.TranslatedColumns;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "foo")
@XmlRootElement(name = "foo")
@Entity(name = Foo.TYPE)
@TranslatedColumns("name")
@Cacheable
public class Foo
    extends PersistableEntity
    implements Versioned {
    private static final long serialVersionUID = -3494592002323415751L;

    /** Foo */
    public static final String TYPE = "Foo";
    private Date date;
    private String name;
    private int value;
    private int version;

    /**
     * Creates a new Foo object.
     */
    public Foo() {
        super();
    }

    /**
     * Creates a new Foo object.
     *
     * @param id
     *            JAVADOC.
     */
    public Foo(Long id) {
        super();
        setId(id);
    }

    /**
     * @param date
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return
     */
    @Temporal(TemporalType.TIMESTAMP)
    public Date getDate() {
        return date;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param name
     *            JAVADOC.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @ProjectionColumn
    public String getName() {
        return name;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param value
     *            JAVADOC.
     */
    public void setValue(int value) {
        this.value = value;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @ProjectionColumn(columnName = "theValue")
    public int getValue() {
        return value;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param version
     *            JAVADOC.
     */
    @Override
    public void setVersion(int version) {
        this.version = version;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    @Version
    public int getVersion() {
        return this.version;
    }
}