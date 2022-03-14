package org.linn.controller;

import java.util.List;
import org.linn.nacos.NacosClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/nacos")
public class NacosController {

	@Autowired
	private NacosClientService nacosClientService;

	@GetMapping("/get")
	public List<ServiceInstance> getService(@RequestParam(defaultValue = "ec-oms-account") String serviceId) {
		return nacosClientService.getNacosClientInfo(serviceId);
	}
}
