package org.cucina.engine.server.communication;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;


/**
 * JAVADOC Interface Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public interface ClientRegistry {
	/**
	 * gets record for the application.
	 *
	 * @param applicationName record name.
	 * @return destination recorded.
	 */
	DestinationDescriptor getDestination(String applicationName);

	public static class DestinationDescriptor
			implements Serializable {
		private static final long serialVersionUID = -1043503115693129009L;
		private static final String URL_SEPARATOR = "://";
		private static final String JMS_PROTOCOL = "jms";
		private String destinationName;
		private String protocol;

		public DestinationDescriptor() {
		}

		public DestinationDescriptor(String string) {
			int index = string.indexOf(URL_SEPARATOR);

			// simple parse
			if (index > -1) {
				protocol = string.substring(0, index);
				destinationName = StringUtils.substringAfter(string, URL_SEPARATOR);
			} else {
				protocol = JMS_PROTOCOL;
				destinationName = string;
			}
		}

		public String getDestinationName() {
			return destinationName;
		}

		public void setDestinationName(String destinationName) {
			this.destinationName = destinationName;
		}

		public String getProtocol() {
			return protocol;
		}

		public void setProtocol(String protocol) {
			this.protocol = protocol;
		}

		/**
		 * the string should be compliant to JMX ObjectName validity and be easy
		 * to view in jconsole.
		 *
		 * @return This object as String.
		 */
		@Override
		public String toString() {
			return "" + protocol + "_//" + destinationName;
		}
	}
}
