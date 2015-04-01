package org.cucina.security.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.cucina.security.validation.UniquePrivilege;
import org.springframework.data.mongodb.core.mapping.Document;


/**
 * Permission object belonging to a {@link Role}.
 *
  */
@Document
public class Privilege
    extends Entity {
    @UniquePrivilege
    @NotNull
    @Size(min = 1, max = 32)
    @Pattern(regexp = "[a-zA-Z0-9_\\-]+", message = "error.alphanumeric")
    private String name;

    /**
     * Set name
     *
     * @param name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get name
     *
     * @return name.
     */
    public String getName() {
        return name;
    }
}
