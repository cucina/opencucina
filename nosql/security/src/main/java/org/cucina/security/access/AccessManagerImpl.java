package org.cucina.security.access;

import org.apache.commons.beanutils.BeanPropertyValueEqualsPredicate;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.cucina.security.bean.InstanceFactory;
import org.cucina.security.model.*;
import org.cucina.security.repository.UserRepository;
import org.cucina.security.service.UserAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.math.BigInteger;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
@Service
public class AccessManagerImpl
		implements AccessManager {
	private static final Logger LOG = LoggerFactory.getLogger(AccessManagerImpl.class);
	private AccessRegistry accessRegistry;
	private InstanceFactory instanceFactory;
	private Map<String, String> suffixByType = new HashMap<String, String>();
	private UserAccessor userAccessor;
	private UserRepository userRepository;

	/**
	 * Creates a new PermissionManagerImpl object.
	 *
	 * @param permissionsHelper  JAVADOC.
	 * @param userRepository     JAVADOC.
	 * @param adminPrivilegeName JAVADOC.
	 */
	@Autowired
	public AccessManagerImpl(InstanceFactory instanceFactory, UserRepository userRepository,
							 UserAccessor userAccessor, AccessRegistry accessRegistry) {
		Assert.notNull(instanceFactory, "instanceFactory is null");
		this.instanceFactory = instanceFactory;
		Assert.notNull(userRepository, "userRepository is null");
		this.userRepository = userRepository;
		Assert.notNull(userAccessor, "userAccessor is null");
		this.userAccessor = userAccessor;
		Assert.notNull(accessRegistry, "accessRegistry is null");
		this.accessRegistry = accessRegistry;
	}

	/**
	 * Checks that the user has the configured privilege.
	 */
	@Override
	public boolean isAdmin() {
		return this.hasPrivilege(accessRegistry.getSystemPrivilege().getName());
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param suffixByType JAVADOC.
	 */
	public void setSuffixByType(Map<String, String> suffixByType) {
		this.suffixByType = suffixByType;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param privilegeName  JAVADOC.
	 * @param typeName       JAVADOC.
	 * @param propertyValues JAVADOC.
	 * @return JAVADOC.
	 */
	@Override
	public boolean hasPermission(String privilegeName, String typeName,
								 Map<String, Object> propertyValues) {
		Assert.hasText(privilegeName, "privilegeName is required!");

		User user = userAccessor.getCurrentUser();

		return userHasPermission(user, privilegeName, typeName, propertyValues);
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param privilegeName JAVADOC.
	 * @param typeName      JAVADOC.
	 * @param entity        JAVADOC.
	 * @return JAVADOC.
	 */
	public boolean hasPermission(String privilegeName, Entity entity) {
		Assert.notNull(entity, "Entity is null");

		Map<String, Object> map;

		try {
			map = new PropertyUtilsBean().describe(entity);
		} catch (Exception e) {
			throw new RuntimeException("Failure to convert object to map", e);
		}

		return hasPermission(privilegeName, entity.getClass().getName(), map);
	}

	/**
	 * Simple implementation which just gets all the user's privileges and
	 * checks if the given privilegeName is among them. (non-Javadoc)
	 *
	 * @param privilegeName JAVADOC.
	 * @return JAVADOC.
	 * @see org.cucina.meringue.spring.security.AccessManager#hasPrivilege(java.lang.String)
	 */
	@Override
	public boolean hasPrivilege(String privilegeName) {
		Assert.hasText(privilegeName, "privilegeName is required!");

		User user = userAccessor.getCurrentUser();

		return userHasPrivilege(privilegeName, user);
	}

	/**
	 * @param privilegeName JAVADOC.
	 * @param filterCurrent JAVADOC.
	 * @return JAVADOC.
	 * @see org.cucina.meringue.spring.security.AccessManager#listUsers(java.lang.String)
	 */
	@Override
	@Transactional(readOnly = true)
	public Collection<User> listActiveUsers(String privilegeName, Boolean filterCurrent) {
		Collection<User> users = userRepository.findAll();
		Collection<User> result = new HashSet<User>();

		if (StringUtils.isNotBlank(privilegeName)) {
			for (User user : users) {
				// Remove Users who don't have permission or are disabled
				if (user.isEnabled() && userHasPrivilege(privilegeName, user)) {
					result.add(user);
				}
			}
		}

		if ((filterCurrent != null) && filterCurrent) {
			result.remove(userAccessor.getCurrentUser());
		}

		return result;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param typeName       JAVADOC.
	 * @param propertyName   JAVADOC.
	 * @param propertyValues JAVADOC.
	 * @return JAVADOC.
	 */
	protected Object getPropertyValue(String typeName, String propertyName,
									  Map<String, Object> propertyValues) {
		String suffix = suffixByType.get(typeName);

		if (StringUtils.isEmpty(suffix)) {
			return propertyValues.get(propertyName);
		}

		Object val = propertyValues.get(propertyName + suffix);

		if (val == null) {
			val = propertyValues.get(propertyName);
		}

		return val;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param typeName       JAVADOC.
	 * @param dimensions     JAVADOC.
	 * @param propertyValues JAVADOC.
	 * @return JAVADOC.
	 */
	protected boolean matches(String typeName, Map<String, Collection<BigInteger>> dimensions,
							  Map<String, Object> propertyValues) {
		int matchCount = 0;

		for (Map.Entry<String, Collection<BigInteger>> entry : dimensions.entrySet()) {
			Object val = getPropertyValue(typeName, entry.getKey(), propertyValues);

			//should have a value, as permissionsHelper.organiseDimensions only returns those
			//properties that are relevant. if one doesn't appear, it means that
			//it's missing on the object, so think we should say no
			if (val == null) {
				LOG.warn("Null value for " + entry.getKey() + " of type " + typeName +
						". Check projections. Disallowing permission check");
			}

			if ((val != null) && compareValues(entry.getValue(), val)) {
				matchCount++;
			}
		}

		return matchCount == dimensions.keySet().size();
	}

	private boolean compareValues(Collection<BigInteger> ref, Object object) {
		if ((object == null) || (ref == null) || ref.isEmpty()) {
			return false;
		}

		BigInteger objectId = null;

		if (object instanceof Long) {
			objectId = BigInteger.valueOf((Long) object);
		} else if (object instanceof Entity) {
			objectId = ((Entity) object).getId();
		} else if (object instanceof BigInteger) {
			objectId = (BigInteger) object;
		}

		return ref.contains(objectId);
	}

	private Map<String, Collection<BigInteger>> relevantDimensions(String typeName,
																   Permission permission) {
		Map<String, Collection<BigInteger>> byProperty = new HashMap<String, Collection<BigInteger>>();

		for (Dimension dimension : permission.getDimensions()) {
			String propertyName = dimension.getPropertyName();

			if (StringUtils.isNotEmpty(instanceFactory.getPropertyType(typeName, propertyName))) {
				Collection<BigInteger> objects = byProperty.get(dimension.getPropertyName());

				if (objects == null) {
					objects = new HashSet<BigInteger>();
					byProperty.put(dimension.getPropertyName(), objects);
				}

				objects.add(dimension.getDomainObjectId());
			}
		}

		return byProperty;
	}

	private boolean userHasPermission(User user, String privilegeName, String typeName,
									  Map<String, Object> propertyValues) {
		for (Permission permission : user.getPermissions()) {
			if (CollectionUtils.find(permission.getRole().getPrivileges(),
					new BeanPropertyValueEqualsPredicate("name", privilegeName)) != null) {
				//found priv match
				Map<String, Collection<BigInteger>> organisedMap = relevantDimensions(typeName,
						permission);

				if (matches(typeName, organisedMap, propertyValues)) {
					return true;
				}
			}
		}

		return false;
	}

	private boolean userHasPrivilege(String privilegeName, User user) {
		Collection<Privilege> privileges = new HashSet<Privilege>();

		for (Permission permission : user.getPermissions()) {
			privileges.addAll(permission.getRole().getPrivileges());
		}

		return CollectionUtils.find(privileges,
				new BeanPropertyValueEqualsPredicate("name", privilegeName)) != null;
	}
}
