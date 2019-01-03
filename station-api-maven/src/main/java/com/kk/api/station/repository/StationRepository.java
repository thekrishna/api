package com.kk.api.station.repository;

import org.springframework.stereotype.Repository;

import com.kk.api.station.model.StationDBModel;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 
 * @author Krishna Kumar
 *
 */
@Repository
public interface StationRepository extends JpaRepository<StationDBModel, String> {
	Optional<StationDBModel> findByStationId(String stationId);
	Optional<StationDBModel> findByName(String name);
	Iterable<StationDBModel> findByHdEnabled(Boolean hdEnabled);

}
