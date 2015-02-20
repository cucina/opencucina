
package org.cucina.core.validation;


/**
 * Validation group, should be controlled by using this in persistence.xml:
 * <code>
 * <property name="javax.persistence.validation.group.pre-persist" value="org.cucina.meringue.validation.Create" />
 * </code> and specifying group in annotation attribute like that <code>
 *
 * @AssertTrue(groups = Create.class) </code>
 */
public interface Create {
}
