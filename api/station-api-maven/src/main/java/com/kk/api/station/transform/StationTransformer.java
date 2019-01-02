package com.kk.api.station.transform;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.kk.api.station.model.Station;
import com.kk.api.station.model.StationDBModel;

/**
 * @author Krishna Kumar
 *
 */
public class StationTransformer {

	/**
	 * Converts API object to DBModel object
	 * 
	 * @param station
	 * @return instance of DBModel object
	 */
	public StationDBModel mapApiToDbModel(final Station station) {
		final StationDBModel stationDBModel = new StationDBModel();
		stationDBModel.setStationId(station.getStationId().toString());
		stationDBModel.setCallSign(station.getCallSign());
		stationDBModel.setName(station.getName());
		stationDBModel.setHdEnabled(station.isHdEnabled());
		return stationDBModel;
	}

	/**
	 * Converts DBModel object to API object
	 * 
	 * @param station
	 * @return instance of API object
	 */
	public Station mapDbModelToApi(final StationDBModel stationDBModel) {
		final Station station = new Station();
		station.setStationId(UUID.fromString(stationDBModel.getStationId()));
		station.setCallSign(stationDBModel.getCallSign());
		station.setName(stationDBModel.getName());
		station.setHdEnabled(stationDBModel.getHdEnabled());
		return station;
	}

	/**
	 * Converts list of DBModel objects to API objects
	 * 
	 * @param iter
	 *            - list of iterable StationDBModel
	 * @return instance of List<Station >API objects
	 */
	public List<Station> mapDbModelToApiItems(final Iterable<StationDBModel> iter) {
		Optional<List<Station>> listToReturn = Optional.empty();
		if (iter != null) {
			listToReturn = Optional.of(StreamSupport.stream(iter.spliterator(), false).map(dbModel -> {
				return mapDbModelToApi(dbModel);
			}).collect(Collectors.toList()));
		}
		return listToReturn.orElse(Collections.emptyList());
	}

}
