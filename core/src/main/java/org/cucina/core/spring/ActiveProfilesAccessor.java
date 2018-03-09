package org.cucina.core.spring;

import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
@Component
public class ActiveProfilesAccessor
		implements EnvironmentAware {
	/**
	 * This is a field JAVADOC
	 */
	public static final String PROFILES_ACCESSOR_ID = "activeProfilesAccessor";
	private Environment environment;
	private String ssoProfileKey = "auth-sso";

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	public String[] getActiveProfiles() {
		String[] ret = environment.getActiveProfiles();

		if (ret == null) {
			ret = new String[]{};
		}

		return ret;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param environment JAVADOC.
	 */
	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	public String getSsoProfileKey() {
		return ssoProfileKey;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param ssoProfileKey JAVADOC.
	 */
	public void setSsoProfileKey(String ssoProfileKey) {
		this.ssoProfileKey = ssoProfileKey;
	}
}
