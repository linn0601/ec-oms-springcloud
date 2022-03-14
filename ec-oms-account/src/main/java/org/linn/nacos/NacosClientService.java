package org.linn.nacos;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

@Service
public class NacosClientService {

	public static final Logger logger = LoggerFactory.getLogger(NacosClientService.class);

	private final DiscoveryClient discoveryClient;

	public NacosClientService(DiscoveryClient discoveryClient) {
		this.discoveryClient = discoveryClient;
	}

	public List<ServiceInstance> getNacosClientInfo(String serviceId) {
		logger.info("request nacos client to get service instance info [{}]", serviceId);
		return discoveryClient.getInstances(serviceId);
	}

}
