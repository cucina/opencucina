package org.cucina.engine.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ClassUtils;
import org.cucina.core.CompositeInstanceFactory;
import org.cucina.core.InstanceFactory;
import org.cucina.core.PackageBasedInstanceFactory;
import org.cucina.core.model.Attachment;
import org.cucina.core.service.ContextService;
import org.cucina.core.service.ThreadLocalContextService;
import org.cucina.engine.DefaultProcessEnvironment;
import org.cucina.engine.ProcessEnvironment;
import org.cucina.engine.TokenFactory;
import org.cucina.engine.definition.config.ProcessDefinitionParser;
import org.cucina.engine.definition.config.ProcessDefinitionRegistry;
import org.cucina.engine.definition.config.xml.DigesterModuleProcessDefinitionParser;
import org.cucina.engine.model.Workflow;
import org.cucina.i18n.api.ListItemService;
import org.cucina.i18n.api.remote.RemoteListNodeService;
import org.cucina.security.api.AccessFacade;
import org.cucina.security.api.remote.RemoteAccessFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;

/**
 * JAVADOC for Class Level
 *
 * @author vlevine
 */
@Configuration
@ComponentScan(basePackages = { "org.cucina.engine", "org.cucina.engine.server" })
public class ProcessConfiguration {
	private static final String SERVER_PROCESS_RULES = "classpath:org/cucina/engine/server/definition/config/xml/workflow-rules-definitions.xml";
	private static final Logger LOG = LoggerFactory
			.getLogger(ProcessConfiguration.class);
	@Value("${org.cucina.access.url}")
	private String accessUrl;
	@Value("${org.cucina.listnode.url}")
	private String listnodeUrl;
	@Value("${org.cucina.workflows.resources}")
	private String processLocation; // = { "classpath:workflows/Item.xml",
									// "classpath:workflows/Report.xml" };

	/**
	 *
	 * @return .
	 */
	@Bean
	public AccessFacade accessFacade() {
		return new RemoteAccessFacade(accessUrl);
	}

	/**
	 *
	 * @return
	 */
	@Bean
	public ContextService contextService() {
		return new ThreadLocalContextService();
	}

	/**
	 * @return .
	 */
	@Bean
	public InstanceFactory instanceFactory() {
		return new CompositeInstanceFactory(new PackageBasedInstanceFactory(
				ClassUtils.getPackageName(Workflow.class)),
				new PackageBasedInstanceFactory(ClassUtils
						.getPackageName(Attachment.class)));
	}

	/**
	 *
	 * @return .
	 */
	@Bean
	public ListItemService listNodeService() {
		return new RemoteListNodeService(listnodeUrl);
	}

	/**
	 * @param applicationContext
	 *            .
	 *
	 * @return .
	 */
	@Bean
	public ProcessDefinitionParser processDefinitionParser(
			ApplicationContext applicationContext) {
		return new DigesterModuleProcessDefinitionParser(
				applicationContext.getResource(SERVER_PROCESS_RULES));
	}

	/**
	 *
	 *
	 * @param tokenFactory
	 * @param applicationContext
	 * @param definitionRegistry
	 *
	 * @return .
	 *
	 * @throws IOException .
	 */
	@Bean
	public ProcessEnvironment processEnvironment(TokenFactory tokenFactory,
			ApplicationContext applicationContext,
			ProcessDefinitionRegistry definitionRegistry) throws IOException {
		DefaultProcessEnvironment dpe = new DefaultProcessEnvironment();

		dpe.setTokenFactory(tokenFactory);
		dpe.setDefinitionResources(findResources(applicationContext,
				processLocation));
		dpe.setDefinitionRegistry(definitionRegistry);
		return dpe;
	}

	private List<Resource> findResources(
			ResourcePatternResolver applicationContext, String prefix)
			throws IOException {
		String path = prefix + "/*.xml";

		if (!applicationContext.getResource(prefix).exists()) {
			LOG.warn("No process definitions were found here '" + path + "'");

			return new ArrayList<Resource>();
		}

		return Arrays.asList(applicationContext.getResources(path));
	}
}
