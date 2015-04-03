package org.cucina.sample.engine.client;

import java.math.BigInteger;

import org.springframework.data.mongodb.core.mapping.Document;


/**
 * 
 *
 * @author vlevine
  */
@Document
public class Item {
    private BigInteger id;
    private String name;

    /**
     *
     *
     * @param id .
     */
    public void setId(BigInteger id) {
        this.id = id;
    }

    /**
     *
     *
     * @return .
     */
    public BigInteger getId() {
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
}
