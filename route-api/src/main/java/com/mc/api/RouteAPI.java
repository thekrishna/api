package com.mc.api;

import org.springframework.web.bind.annotation.RequestParam;

import com.mc.model.Graph;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "RouteAPI")
public interface RouteAPI {
	@ApiOperation(value = "Determines if two cities are connected.", nickname = "IsConnected", response = Graph.class, tags = {
			"Route", })
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Checks is two cities are connected?", response = Graph.class),
			@ApiResponse(code = 404, message = "Source or Destination not found"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	String isConnected(@ApiParam(value = "Origin of the route", required = true) @RequestParam("origin") String origin,
			@ApiParam(value = "Destination of the route", required = true) @RequestParam("destination") String destination);

}
