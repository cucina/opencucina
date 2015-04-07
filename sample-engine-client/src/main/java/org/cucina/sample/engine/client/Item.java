package org.cucina.sample.engine.client;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


/**
 *
 *
 * @author vlevine
  */
@Entity
public class Item {
    private Long id;
    private String name;
    private String status;

    /**
     *
     *
     * @param id .
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     *
     *
     * @return .
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    /**
     *
     *
     * @param name .
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     *
     * @return .
     */
    public String getName() {
        return name;
    }

    /**
     *
     *
     * @param status .
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     *
     *
     * @return .
     */
    public String getStatus() {
        return status;
    }
}
