package com.kk.api.station.controller;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.kk.api.station.model.Station;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2019-01-02T02:13:32.583+05:30")

@Controller
public class StationsApiController implements StationsApi {
	private static final Logger log = LoggerFactory.getLogger(StationsApiController.class);

	@Autowired
	public StationsApiController() {
	}
	
	@Override
	public ResponseEntity<Void> addStation(@Valid @RequestBody final Station station) {
		return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
	}
	
	@Override
	public ResponseEntity<Void> deleteStation(@NotNull @PathVariable("stationId") final UUID stationId,
			@Valid @RequestBody final Station station) {
		return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
	}
	
	@Override
	public ResponseEntity<List<Station>> findHdStations() {
		return new ResponseEntity<List<Station>>(HttpStatus.NOT_IMPLEMENTED);
	}
	
	@Override
	public ResponseEntity<Station> findStationById(@NotNull @PathVariable("stationId") final UUID stationId) {
		return new ResponseEntity<Station>(HttpStatus.NOT_IMPLEMENTED);
	}
	
	@Override
	public ResponseEntity<Station> findStationByName(
			@NotNull @Valid @RequestParam(value = "name", required = true) final String name) {
		return new ResponseEntity<Station>(HttpStatus.NOT_IMPLEMENTED);
	}
	
	@Override
	public ResponseEntity<List<Station>> listStation() {
		return new ResponseEntity<List<Station>>(HttpStatus.NOT_IMPLEMENTED);
	}
	
	@Override
	public ResponseEntity<Station> updateStation(@PathVariable("stationId") final UUID stationId,
			@Valid @RequestBody final Station station) {
		return new ResponseEntity<Station>(HttpStatus.NOT_IMPLEMENTED);
	}

}
