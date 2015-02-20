
package org.cucina.core.validation;


/**
 * Validation group, should be controlled by using this in persistence.xml:
 * <code>
 * <property name="javax.persistence.validation.group.pre-remove" value="org.cucina.meringue.validation.Delete" />
 * </code> and specifying group in annotation attribute like that <code>
 *
 * @AssertTrue(groups = Delete.class) </code>
 */
public interface Delete {
}
