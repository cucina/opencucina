package org.cucina.engine.definition;

import org.springframework.data.domain.Persistable;


/**
 * JAVADOC Interface Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public interface Token {
    /** Token. */
    String APPLICATION_TYPE = "Token";

    /** domainObjectId */
    String DOMAIN_OBJ_ID_NAME = "domainObjectId";

    /** domainObjectType */
    String DOMAIN_OBJ_TYPE_NAME = "domainObjectType";

    /** loadDomainIdsQuery */
    String LOAD_DOMAIN_IDS_NAMED_QUERY = "loadDomainIdsQuery";

    /** loadTokenQuery */
    String LOAD_TOKEN_NAMED_QUERY = "loadTokenQuery";

    /** placeId */
    String PLACE_ID_PROPERTY_NAME = "placeId";

    /** workflowDefinitionId */
    String WFD_ID_PROPERTY_NAME = "workflowDefinitionId";

    /**
     * Sets the domain object this is linked to
     *
     * @param domainObject JAVADOC.
     */
    void setDomainObject(Persistable<Long> domainObject);

    /**
     * Gets the domain object this is linked to
     *
     * @return JAVADOC.
     */
    Persistable<Long> getDomainObject();

    /**
     * Sets parent token
     *
     * @param token JAVADOC.
     */
    void setParentToken(Token token);

    /**
     * Gets parent token
     *
     * @return JAVADOC.
     */
    Token getParentToken();

    /**
     * Sets this token's current place's id
     *
     * @param placeId
     * TODO map it to the real Place
     */
    void setPlaceId(final String placeId);

    /**
     * Gets this token's current place's id
     *
     * @return JAVADOC
     */
    String getPlaceId();

    /**
     * Sets this token's process definition id
     *
     * @param processDefinitionId JAVADOC.
     */
    void setProcessDefinitionId(String processDefinitionId);

    /**
     * Gets this token's process definition id
     *
     * @return JAVADOC.
     */
    String getProcessDefinitionId();

    /**
     * Adds a child token
     *
     * @param child JAVADOC.
     */
    void addChild(Token child);

    /**
     *
     *
     * @return whether this has children
     */
    boolean hasChildren();

    /**
     * Removes the child from its collection of chidlren
     *
     * @param child JAVADOC.
     */
    void removeChild(Token child);
}
