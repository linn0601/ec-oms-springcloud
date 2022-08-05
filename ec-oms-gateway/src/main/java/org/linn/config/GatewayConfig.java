package org.linn.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 用于将网关路由配置放置到nacos配置中心
 * <li>当网关配置发生变化时nacos会通知gateway刷新配置</li>
 */
@Configuration("gatewayConfig")
public class GatewayConfig {

	/**
	 * 读取配置超时时间
	 */
	public static final long DEFAULT_TIMEOUT = 3000;

	/**
	 * nacos 服务地址
	 */
	private String nacosServerAddress;

	/**
	 * nacos 命名空间
	 */
	private String nacosNamespace;

	/**
	 * nacos dataId
	 */
	private String nacosDataId;

	/**
	 * groupId
	 */
	private String nacosGroup;

	@Value("${spring.cloud.nacos.discovery.server-addr}")
	public void setNacosServerAddress(String nacosServerAddress) {
		this.nacosServerAddress = nacosServerAddress;
	}

	@Value("${spring.cloud.nacos.discovery.namespace}")
	public void setNacosNamespace(String nacosNamespace) {
		this.nacosNamespace = nacosNamespace;
	}

	@Value("${nacos.gateway.data-id}")
	public void setNacosDataId(String nacosDataId) {
		this.nacosDataId = nacosDataId;
	}

	@Value("${nacos.gateway.group-id}")
	public void setNacosGroup(String nacosGroup) {
		this.nacosGroup = nacosGroup;
	}

	public String getNacosServerAddress() {
		return nacosServerAddress;
	}

	public String getNacosNamespace() {
		return nacosNamespace;
	}

	public String getNacosDataId() {
		return nacosDataId;
	}

	public String getNacosGroup() {
		return nacosGroup;
	}
}
