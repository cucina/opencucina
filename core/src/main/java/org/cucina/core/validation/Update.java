
package org.cucina.core.validation;


/**
 * Validation group, should be controlled by using this in persistence.xml:
 * <code>
 * <property name="javax.persistence.validation.group.pre-update" value="org.cucina.meringue.validation.Update" />
 * </code> and specifying group in annotation attribute like that <code>
 *
 * @AssertTrue(groups = Update.class) </code>
 */
public interface Update {
}
