package org.cucina.security.bean;

import java.beans.PropertyEditorSupport;

import java.util.Collection;
import java.util.HashSet;

import org.apache.commons.lang3.StringUtils;

import org.springframework.util.Assert;

import org.cucina.security.model.Privilege;
import org.cucina.security.repository.PrivilegeRepository;


/**
 * Extension of PropertyEditorSupport to convert csv of Role names into Roles.
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class PrivilegesEditor
    extends PropertyEditorSupport {
    private PrivilegeRepository privilegeRepository;

    /**
         * Creates a new RolesEditor object.
         *
         * @param rolePrivilegeDao JAVADOC.
         */
    public PrivilegesEditor(PrivilegeRepository privilegeRepository) {
        Assert.notNull(privilegeRepository, "privilegeRepository cannot be null");
        this.privilegeRepository = privilegeRepository;
    }

    /**
     * Converts csv roles into populated Role instances, loads from persistence.
     *
     * @param text String csv role names.
     *
     * @throws IllegalArgumentException.
     */
    @Override
    public void setAsText(String text)
        throws IllegalArgumentException {
        Collection<Privilege> value = new HashSet<Privilege>();

        if (StringUtils.isNotEmpty(text)) {
            String[] privilegeNames = text.split(",");

            if ((privilegeNames != null) && (privilegeNames.length > 0)) {
                for (int i = 0; i < privilegeNames.length; i++) {
                    String privilegeName = privilegeNames[i].trim();
                    Privilege privilege = privilegeRepository.findByName(privilegeName);

                    if (privilege == null) {
                        throw new IllegalArgumentException("Invalid privilege '" +
                            privilegeNames[i] + "'");
                    }

                    value.add(privilege);
                }
            }
        }

        setValue(value);
    }
}
