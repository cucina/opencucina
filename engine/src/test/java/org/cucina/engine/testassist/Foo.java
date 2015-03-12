package org.cucina.engine.testassist;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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

import org.cucina.i18n.model.Message;


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
    private Collection<Bar> bars;
    private Collection<Baz> bazs;
    private Date date;
    private Message message;
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
     * JAVADOC Method Level Comments
     *
     * @param bars
     *            JAVADOC.
     */
    public void setBars(Collection<Bar> bars) {
        this.bars = bars;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "foo")
    public Collection<Bar> getBars() {
        return bars;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param bazs
     *            JAVADOC.
     */
    public void setBazs(Collection<Baz> bazs) {
        this.bazs = bazs;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "foo")
    public Collection<Baz> getBazs() {
        return bazs;
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
     * @param message
     *            JAVADOC.
     */
    public void setMessage(Message message) {
        this.message = message;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @ManyToOne
    public Message getMessage() {
        return message;
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
