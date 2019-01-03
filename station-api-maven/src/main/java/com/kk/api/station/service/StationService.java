package com.kk.api.station.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kk.api.station.exception.StationApiError;
import com.kk.api.station.exception.StationApiException;
import com.kk.api.station.model.Station;
import com.kk.api.station.model.StationDBModel;
import com.kk.api.station.repository.StationRepository;
import com.kk.api.station.transform.StationTransformer;

/**
 * 
 * @author Krishna Kumar
 *
 */
@Service
public class StationService {
	private StationRepository stationRepository;
	private StationTransformer stationTransformer;

	@Autowired
	public StationService(final StationRepository stationRepository, final StationTransformer stationTransformer) {
		this.stationRepository = stationRepository;
		this.stationTransformer = stationTransformer;
	}

	/**
	 * This method enables to list all stations.
	 * 
	 * @return List<Station>
	 */
	public List<Station> listAllStations() {
		final Iterable<StationDBModel> iter = stationRepository.findAll();
		return this.stationTransformer.mapDbModelToApiItems(iter);
	}

	/**
	 * Creates new station in the database.
	 * 
	 * @param station
	 *            - the station to be created
	 * @return id of the newly created station
	 * @throws StationApiException
	 *             - if station already exists
	 */
	public String addStation(final Station station) {
		final Optional<StationDBModel> response = stationRepository.findByName(station.getName());
		if (response.isPresent()) {
			throw new StationApiException(StationApiError.ALREADY_EXISTS,
					StationApiException.StationApiResponseCode.CONFLICT);
		}
		final StationDBModel toInsert = this.stationTransformer.mapApiToDbModel(station);
		final StationDBModel toReturn = this.stationRepository.save(toInsert);
		return toReturn.getStationId();
	}

	/**
	 * Updates existing station in the database.
	 * 
	 * @param stationId
	 *            - the station ID to be updated
	 * @param station
	 *            - the station to be updated
	 * @return updated instance of Station
	 * @throws StationApiException
	 *             - if station not found
	 */
	public Station updateStation(final UUID stationId, final Station station) {
		final Optional<StationDBModel> response = stationRepository.findByStationId(stationId.toString());

		final StationDBModel toUpdate = response.orElseThrow(() -> new StationApiException(StationApiError.NOT_FOUND,
				StationApiException.StationApiResponseCode.NOT_FOUND));

		toUpdate.setName(station.getName());
		toUpdate.setCallSign(station.getCallSign());
		toUpdate.setHdEnabled(station.isHdEnabled());

		final StationDBModel fromDb = this.stationRepository.save(toUpdate);
		return this.stationTransformer.mapDbModelToApi(fromDb);
	}

	/**
	 * Deletes existing station from the database.
	 * 
	 * @param stationId
	 *            - the station ID to be deleted
	 * @param station
	 *            - the station to be deleted
	 * @throws StationApiException
	 *             - if station not found
	 */
	public void deleteStation(final UUID stationId, final Station station) {
		final Optional<StationDBModel> response = stationRepository.findByStationId(stationId.toString());
		final StationDBModel toDelete = response.orElseThrow(() -> new StationApiException(StationApiError.NOT_FOUND,
				StationApiException.StationApiResponseCode.NOT_FOUND));
		this.stationRepository.save(toDelete);
	}

	/**
	 * This method enables to list all HD enabled stations.
	 * 
	 * @return List<Station>
	 */
	public List<Station> findHdEnabledStations() {
		final Iterable<StationDBModel> iter = stationRepository.findByHdEnabled(true);
		return this.stationTransformer.mapDbModelToApiItems(iter);
	}

	/**
	 * Gets the station details corresponds to the stationId passed as parameter
	 * from the database.
	 * 
	 * @param stationId
	 *            - the station ID to be fetched.
	 * @return instance of Station
	 * @throws StationApiException
	 *             - if station not found
	 */
	public Station findByStationId(final UUID stationId) {
		final Optional<StationDBModel> response = stationRepository.findByStationId(stationId.toString());
		final StationDBModel stationDBModel = response
				.orElseThrow(() -> new StationApiException(StationApiError.NOT_FOUND,
						StationApiException.StationApiResponseCode.NOT_FOUND));
		return this.stationTransformer.mapDbModelToApi(stationDBModel);
	}

	/**
	 * Gets the station details corresponds to the stationId passed as parameter
	 * from the database.
	 * 
	 * @param stationId
	 * @return instance of Station
	 * @throws StationApiException
	 *             - if station not found
	 */
	public Station findByStationName(final String stationName) {
		final Optional<StationDBModel> response = stationRepository.findByName(stationName);
		final StationDBModel stationDBModel = response
				.orElseThrow(() -> new StationApiException(StationApiError.NOT_FOUND,
						StationApiException.StationApiResponseCode.NOT_FOUND));
		return this.stationTransformer.mapDbModelToApi(stationDBModel);
	}

}