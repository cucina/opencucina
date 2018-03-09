package org.cucina.core.marshal;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Map;


/**
 * Marshaller implementation using Jackson JSON object mapper
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class JacksonMarshaller
		implements Marshaller, InitializingBean {
	private static final Logger LOG = LoggerFactory.getLogger(JacksonMarshaller.class);
	private Collection<JsonSerializer<?>> serialisers;
	@SuppressWarnings("rawtypes")
	private Map<Class, JsonDeserializer> deserialisers;

	//Is thread safe as long as not configured
	private ObjectMapper mapper = new ObjectMapper();

	/**
	 * Creates a new JacksonMarshaller object.
	 *
	 * @param persistenceService JAVADOC.
	 */
	public JacksonMarshaller(Collection<JsonSerializer<?>> serialisers,
							 @SuppressWarnings("rawtypes")
									 Map<Class, JsonDeserializer> deserialisers) {
		super();
		this.serialisers = serialisers;
		this.deserialisers = deserialisers;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @throws Exception JAVADOC.
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public void afterPropertiesSet()
			throws Exception {
		//Set up
		SimpleModule module = new SimpleModule("user",
				new Version(1, 0, 0, "User serializer/deserializer", null, null));

		if (CollectionUtils.isNotEmpty(serialisers)) {
			for (JsonSerializer<?> serialiser : serialisers) {
				module.addSerializer(serialiser);
			}
		}

		if (MapUtils.isNotEmpty(deserialisers)) {
			for (Map.Entry<Class, JsonDeserializer> deserialiserEntry : deserialisers.entrySet()) {
				module.addDeserializer(deserialiserEntry.getKey(), deserialiserEntry.getValue());
			}
		}

		mapper.registerModule(module);
	}

	/**
	 * marshalls the object graph.
	 *
	 * @param graph Object.
	 * @return marshalled graph String.
	 */
	@Override
	public String marshall(Object graph) {
		Assert.notNull(graph, "Should provide graph as argument");

		try {
			return mapper.writeValueAsString(graph);
		} catch (Exception e) {
			LOG.warn("Marshalling failed for [" + graph + "]", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Unmarshall the source object to an object of the targetClass.
	 *
	 * @param <T>         Object type to convert to.
	 * @param source      String.
	 * @param targetClass Class.
	 * @return unmarshalled source.
	 */
	@Override
	public <T> T unmarshall(String source, Class<T> targetClass) {
		Assert.notNull(source, "Should provide source as argument");
		Assert.notNull(targetClass, "Should provide targetClass as argument");

		try {
			return mapper.readValue(source, targetClass);
		} catch (Exception e) {
			LOG.warn("Unmarshalling failed for [" + source + "]", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param <T>          JAVADOC.
	 * @param source       JAVADOC.
	 * @param valueTypeRef JAVADOC.
	 * @return JAVADOC.
	 */
	@Override
	public <T> T unmarshall(String source, TypeReference<T> valueTypeRef) {
		Assert.notNull(source, "source is required!");
		Assert.notNull(valueTypeRef, "valueTypeRef is required!");

		try {
			return mapper.<T>readValue(source, valueTypeRef);
		} catch (Exception e) {
			LOG.warn("Unmarshalling failed for [" + source + "]", e);
			throw new RuntimeException(e);
		}
	}
}
