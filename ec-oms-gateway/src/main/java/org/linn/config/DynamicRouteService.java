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

@Service
@SuppressWarnings("all")
public class DynamicRouteService implements ApplicationEventPublisherAware {

	private static final Logger logger = LoggerFactory.getLogger(DynamicRouteService.class);
	private final RouteDefinitionWriter routeDefinitionWriter;
	private final RouteDefinitionLocator routeDefinitionLocator;
	private ApplicationEventPublisher publisher;

	public DynamicRouteService(RouteDefinitionWriter routeDefinitionWriter,
							   RouteDefinitionLocator routeDefinitionLocator) {
		this.routeDefinitionWriter = routeDefinitionWriter;
		this.routeDefinitionLocator = routeDefinitionLocator;
	}

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.publisher = applicationEventPublisher;
	}

	public String updateList(List<RouteDefinition> routeDefinitions) {
		List<RouteDefinition> routeDefinitionList = routeDefinitionLocator.getRouteDefinitions().buffer().blockFirst();
		if (CollectionUtils.isNotEmpty(routeDefinitionList)) {
			routeDefinitionList.forEach(routeDefinition -> {
				this.deleteById(routeDefinition.getId());
			});
		}else {
			routeDefinitions.forEach(routeDefinition -> {
				this.update(routeDefinition);
			});
		}
		this.publisher.publishEvent(new RefreshRoutesEvent(this));

		return "update routeDefubutuibs success";
	}

	public String addRouteDefinition(RouteDefinition routeDefinition) {
		logger.info("gateway add route: [{}]", routeDefinition);
		// ??????????????????????????????
		this.routeDefinitionWriter.save(Mono.just(routeDefinition)).subscribe();

		// ??????????????????,??????????????????????????? [gateway?????????????????????]
		this.publisher.publishEvent(new RefreshRoutesEvent(this));

		return "add routeDefubutuib success";
	}

	/**
	 * ????????????id????????????
	 *
	 * @param id ??????id
	 */
	private String deleteById(String id) {
		try {
			this.routeDefinitionWriter.delete(Mono.just(id)).subscribe();
			// ??????????????????
			this.publisher.publishEvent(new RefreshRoutesEvent(this));

			return "delete routeDefubutuib success";
		}
		catch (Exception e) {
			//todo log
			return "fail";
		}
	}

	public String update(RouteDefinition routeDefinition) {
		// ?????????????????????
		try {
			this.routeDefinitionWriter.delete(Mono.just(routeDefinition.getId())).subscribe();
			this.routeDefinitionWriter.save(Mono.just(routeDefinition)).subscribe();
			this.publisher.publishEvent(new RefreshRoutesEvent(this));
			return "update routeDefubutuib success";
		}
		catch (Exception e) {
			// todo log
			return "fail";
		}
	}
}
