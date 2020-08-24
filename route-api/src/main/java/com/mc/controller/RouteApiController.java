package com.mc.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.mc.api.RouteAPI;
import com.mc.service.RouteService;

@RestController
public class RouteApiController implements RouteAPI {
	private static final Logger LOGGER = LoggerFactory.getLogger(RouteApiController.class);
	
	private RouteService routeService;

	public RouteApiController(RouteService routeService) {
		this.routeService = routeService;
	}

	@GetMapping("/connected")
	@ResponseStatus(code = HttpStatus.OK)
	public String isConnected(@RequestParam final String origin, @RequestParam final String destination) {
		LOGGER.info("Connected? Origin:{} and Destination:{}", origin, destination);
		return routeService.isConnected(origin, destination);
	}

}
