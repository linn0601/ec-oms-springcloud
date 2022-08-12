package org.linn.config;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executor;
import javax.annotation.PostConstruct;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

/**
 * nacos
 */
@Component
// 另一bean初始化后再去初始化，一个依赖注解
@DependsOn(value = { "gatewayConfig" })
public class DynamicRouteServiceLister {

	private static final Logger logger = LoggerFactory.getLogger(DynamicRouteServiceLister.class);

	private static final Gson gson = new GsonBuilder().create();

	/**
	 * nacos 配置服务客户端
	 */
	private ConfigService configService;
	private final GatewayConfig gatewayConfig;
	private final DynamicRouteService dynamicRouteService;

	public DynamicRouteServiceLister(GatewayConfig gatewayConfig,
									 DynamicRouteService dynamicRouteService) {
		this.gatewayConfig = gatewayConfig;
		this.dynamicRouteService = dynamicRouteService;
	}

	/**
	 * 初始化nacos config
	 */
	private ConfigService initConfigService() {
		try {
			Properties properties = new Properties();
			properties.setProperty("serverAddr", gatewayConfig.getNacosServerAddress());
			properties.setProperty("namespace", gatewayConfig.getNacosNamespace());
			configService = NacosFactory.createConfigService(properties);
			return configService;
		}
		catch (Exception e) {
			logger.error("init gateway nacos config error: [{}]", e.getMessage(), e);
		}
		return null;
	}

	/**
	 * Bean在容器中构建完成后会执行init() dateId、groupId
	 */
	@SuppressWarnings("UnstableApiUsage")
	@PostConstruct
	public void init() {
		try {
			configService = initConfigService();
			if (configService != null) {

				String configInfo = configService.getConfig(gatewayConfig.getNacosDataId(),
					gatewayConfig.getNacosGroup(), GatewayConfig.DEFAULT_TIMEOUT);

				logger.info("get current gateway config : [[  {} ]] ", configInfo);
				Type type = new TypeToken<List<RouteDefinition>>() {
				}.getType();
				List<RouteDefinition> routeDefinitions = gson.fromJson(configInfo, type);

				if (CollectionUtils.isNotEmpty(routeDefinitions)) {
					for (var routeDefinition : routeDefinitions) {
						dynamicRouteService.addRouteDefinition(routeDefinition);
					}
				}
			}
		}
		catch (Exception e) {
			logger.error("getaway route init has some error: [{}]", e.getMessage(), e);
		}
		// 设置监听器，当dataId发生变更时进行同步
		dynamicRouteByNacosListener(gatewayConfig.getNacosDataId(), gatewayConfig.getNacosGroup());
	}

	/**
	 * 监听 nacos 下发的动态路由配置
	 */
	@SuppressWarnings("UnstableApiUsage")
	private void dynamicRouteByNacosListener(String dataId, String group) {
		try {
			// 当指定配置发生变化时进行通知，增加一个监听器
			configService.addListener(dataId, group, new Listener() {
				@Override
				public Executor getExecutor() {
					// 使用默认的线程池来监听
					return null;
				}

				/**
				 * 监听器收到配置变更信息
				 * @param configInfo nacos最新配置定义
				 */
				@Override
				public void receiveConfigInfo(String configInfo) {
					logger.info("start to update config [{}] ", configInfo);

					Type routeDefinitionList = new TypeToken<List<RouteDefinition>>() {
					}.getType();
					List<RouteDefinition> routeDefinitions = gson.fromJson(configInfo, routeDefinitionList);

					dynamicRouteService.updateList(routeDefinitions);
				}
			});
		}
		catch (NacosException e) {
			logger.error("dynamic update gateway config error: [{}]", e.getMessage(), e);
		}
	}
}
