package org.cucina.sample.engine.client.app;

import java.util.Collection;
import java.util.Map;

public interface ClientService {
	Item create();

	Collection<Map<String,String>> loadTransitionInfo(String processName);
}
