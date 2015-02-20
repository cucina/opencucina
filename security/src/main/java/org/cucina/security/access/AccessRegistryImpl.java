package org.cucina.security.access;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.cucina.core.utils.NameUtils;
import org.cucina.security.model.Privilege;
import org.cucina.security.repository.PrivilegeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class AccessRegistryImpl
    implements AccessRegistry {
    private static final Logger LOG = LoggerFactory.getLogger(AccessRegistryImpl.class);
    private Map<String, String> typeToPriv = new HashMap<String, String>();
    private PrivilegeRepository privilegeRepository;
    private String defaultPrivilegeName = "VIEW";
    private String systemPrivilegeName = "SYSTEM_ONLY";

    /**
     * Creates a new AccessRegistryImpl object.
     *
     * @param rolePrivilegeDao JAVADOC.
     */
    public AccessRegistryImpl(PrivilegeRepository privilegeRepository) {
        Assert.notNull(privilegeRepository, "privilegeRepository is null");
        this.privilegeRepository = privilegeRepository;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    public Privilege getDefaultPrivilege() {
        if (StringUtils.isNotEmpty(defaultPrivilegeName)) {
            return privilegeRepository.findByName(defaultPrivilegeName);
        }

        LOG.warn("Default privilege not configured, returning null");

        return null;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param defaultPrivilegeName JAVADOC.
     */
    public void setDefaultPrivilegeName(String defaultPrivilegeName) {
        this.defaultPrivilegeName = defaultPrivilegeName;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    public Privilege getSystemPrivilege() {
        if (StringUtils.isNotEmpty(systemPrivilegeName)) {
            return privilegeRepository.findByName(systemPrivilegeName);
        }

        LOG.warn("System privilege not configured, returning null");

        return null;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param systemPrivilegeName JAVADOC.
     */
    public void setSystemPrivilegeName(String systemPrivilegeName) {
        this.systemPrivilegeName = systemPrivilegeName;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param typeToPriv JAVADOC.
     */
    public void setTypeToPriv(Map<String, String> typeToPriv) {
        this.typeToPriv = typeToPriv;
    }

    /**
    * JAVADOC Method Level Comments
    *
    * @param applicationType JAVADOC.
    * @param accessLevel JAVADOC.
    *
    * @return JAVADOC.
    */
    @Override
    public Privilege lookup(String applicationType, String accessLevel) {
        Assert.notNull(applicationType, "should not be null");
        Assert.notNull(accessLevel, "should not be null");

        String priv = typeToPriv.get(NameUtils.concat(applicationType, accessLevel));

        if (StringUtils.isNotBlank(priv)) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Found priv [" + priv + "] for appType [" + applicationType +
                    "] and accessLevel [" + accessLevel + "]");
            }

            return privilegeRepository.findByName(priv);
        }

        priv = typeToPriv.get(applicationType);

        if (StringUtils.isNotBlank(priv)) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Found priv [" + priv + "] for appType [" + applicationType + "]");
            }

            return privilegeRepository.findByName(priv);
        }

        if (LOG.isInfoEnabled()) {
            LOG.info("Defaulting to default privilege for appType [" + applicationType +
                "] and accessLevel [" + accessLevel + "]");
        }

        return getDefaultPrivilege();
    }
}
