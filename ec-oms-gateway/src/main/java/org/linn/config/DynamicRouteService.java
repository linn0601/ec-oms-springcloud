package org.linn.config;

import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * @see ApplicationEventPublisherAware 实现spring的事件接口
 */
@Service
@SuppressWarnings("all")
public class DynamicRouteService implements ApplicationEventPublisherAware {

	private static final Logger logger = LoggerFactory.getLogger(DynamicRouteService.class);
	// 写路由定义的一个工具类
	private final RouteDefinitionWriter routeDefinitionWriter;
	// 获取路由定义
	private final RouteDefinitionLocator routeDefinitionLocator;
	// 事件发布对象
	private ApplicationEventPublisher publisher;

	public DynamicRouteService(RouteDefinitionWriter routeDefinitionWriter,
							   RouteDefinitionLocator routeDefinitionLocator) {
		this.routeDefinitionWriter = routeDefinitionWriter;
		this.routeDefinitionLocator = routeDefinitionLocator;
	}

	/**
	 * spring帮我们注入了applicationEventPublisher
	 * <li>RefreshRoutesEvent 一个事件告知gateway需要拉去配置</li>
	 */
	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.publisher = applicationEventPublisher;
	}

	/**
	 * 批量更新路由定义
	 *
	 * @param routeDefinitions 新的路由定义
	 */
	public void updateList(List<RouteDefinition> routeDefinitions) {
		List<RouteDefinition> routeDefinitionList = routeDefinitionLocator.getRouteDefinitions()
														.buffer()
														.blockFirst();
		if (CollectionUtils.isNotEmpty(routeDefinitionList)) {
			routeDefinitionList.forEach(routeDefinition -> {
				logger.info("delete by id : {} routeDefubutuib success", routeDefinition.getId());
				this.deleteById(routeDefinition.getId());
			});
		}
		routeDefinitions.forEach(routeDefinition -> this.updateRouteDefinition(routeDefinition));
		// 发布事件通知给gatway，同步更新路由定义
		this.publisher.publishEvent(new RefreshRoutesEvent(this));
		logger.info("update [{}] routeDefubutuibs success", routeDefinitionList);
	}

	/**
	 * 添加路由配置
	 *
	 * @param routeDefinition 路由定义对象(将nacos中配置映射成 RouteDefinition对象)
	 */
	public void addRouteDefinition(RouteDefinition routeDefinition) {
		logger.info("gateway add route: [{}]", routeDefinition);
		// 保存路由配置并且发布
		this.routeDefinitionWriter.save(Mono.just(routeDefinition)).subscribe();

		// 发布事件通知,同步新增的路由定义 [gateway定义了一个事件]
		this.publisher.publishEvent(new RefreshRoutesEvent(this));
		logger.info("add [id : {}] routeDefubutuib success", routeDefinition.getId());
	}

	/**
	 * 根据路由id删除配置
	 *
	 * @param id 路由id
	 */
	private void deleteById(String id) {
		try {
			this.routeDefinitionWriter.delete(Mono.just(id)).subscribe();
			// 发布更新事件
			this.publisher.publishEvent(new RefreshRoutesEvent(this));
			logger.info("delete by id : {} routeDefubutuib success", id);
		}
		catch (Exception e) {
			logger.error("delete routeDefubutuib fail, case : {} ", e.getMessage());
		}
	}

	/**
	 * 更新路由定义（先删除再保存）
	 *
	 * @param routeDefinition 新的路由定义对象
	 */
	public void updateRouteDefinition(RouteDefinition routeDefinition) {
		try {
			this.routeDefinitionWriter.delete(Mono.just(routeDefinition.getId())).subscribe();
			this.routeDefinitionWriter.save(Mono.just(routeDefinition)).subscribe();
			this.publisher.publishEvent(new RefreshRoutesEvent(this));
			logger.info("update [id : {}] routeDefubutuib success", routeDefinition.getId());
		}
		catch (Exception e) {
			logger.error("update routeDefubutuib fail, case : {} ", e.getMessage());
		}
	}
}
