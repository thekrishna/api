package com.kk.api.station.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.kk.api.station.StationApiApplication;
import com.kk.api.station.model.StationDBModel;
import com.kk.api.station.repository.StationRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { StationApiApplication.class })
public class StationRepositoryTest {

	private static final String STATION_1 = "STATION_1";
	private static final String STATION_2 = "STATION_2";
	private static final String STATION_3 = "STATION_3";
	private static final String STATION_4 = "STATION_4";
	private static final String STATION_5 = "STATION_5";
	
	private static final String CALL_SIGN_1 = "CALL_SIGN_1";
	private static final String CALL_SIGN_2 = "CALL_SIGN_2";
	private static final String CALL_SIGN_3 = "CALL_SIGN_3";
	private static final String CALL_SIGN_4 = "CALL_SIGN_4";
	private static final String CALL_SIGN_5 = "CALL_SIGN_5";
	
	private static final Boolean TRUE = Boolean.TRUE;
	private static final Boolean FALSE = Boolean.FALSE;

	@Autowired
	private StationRepository stationRepository;

	@Before
	public void init() {
		stationRepository.deleteAll();
	}

	@Test
	public void test_add_station() {
		final StationDBModel stationDBModel = new StationDBModel();
		stationDBModel.setName(STATION_1);
		stationDBModel.setHdEnabled(TRUE);
		stationDBModel.setCallSign(CALL_SIGN_1);
		stationRepository.save(stationDBModel);

		final List<StationDBModel> stations = stationRepository.findAll();
		assertEquals(1, stations.size());

		assertEquals(STATION_1, stationDBModel.getName());
		assertEquals(CALL_SIGN_1, stationDBModel.getCallSign());
		assertEquals(TRUE, stationDBModel.getHdEnabled());
	}

	@Test
	public void test_delete_station() {
		final StationDBModel stationDBModel = new StationDBModel();
		stationDBModel.setName(STATION_2);
		stationDBModel.setHdEnabled(TRUE);
		stationDBModel.setCallSign(CALL_SIGN_2);
		stationRepository.save(stationDBModel);

		List<StationDBModel> foundStations = stationRepository.findAll();
		assertEquals(1, foundStations.size());

		stationRepository.delete(foundStations.get(0));

		foundStations = stationRepository.findAll();
		assertEquals(0, foundStations.size());
	}

	@Test
	public void test_update_station() {
		final StationDBModel stationDBModel = new StationDBModel();
		stationDBModel.setName(STATION_3);
		stationDBModel.setHdEnabled(TRUE);
		stationDBModel.setCallSign(CALL_SIGN_3);
		stationRepository.save(stationDBModel);
		
		stationDBModel.setHdEnabled(false);
		stationRepository.save(stationDBModel);
		
		assertEquals(STATION_3, stationDBModel.getName());
		assertEquals(CALL_SIGN_3, stationDBModel.getCallSign());
		assertEquals(FALSE, stationDBModel.getHdEnabled());
	}
	
	@Test
	public void test_findByStationId() {
		final StationDBModel stationDBModel = new StationDBModel();
		stationDBModel.setName(STATION_4);
		stationDBModel.setHdEnabled(TRUE);
		stationDBModel.setCallSign(CALL_SIGN_4);
		final StationDBModel insertedDBModel = stationRepository.save(stationDBModel);

		final StationDBModel fromDB = stationRepository.findByStationId(insertedDBModel.getStationId()).get();
		assertEquals(STATION_4, fromDB.getName());
		assertEquals(CALL_SIGN_4, fromDB.getCallSign());
		assertEquals(TRUE, fromDB.getHdEnabled());
	}
	
	@Test
	public void test_findByStationName() {
		final StationDBModel stationDBModel = new StationDBModel();
		stationDBModel.setName(STATION_5);
		stationDBModel.setHdEnabled(TRUE);
		stationDBModel.setCallSign(CALL_SIGN_5);
		final StationDBModel insertedDBModel = stationRepository.save(stationDBModel);

		final StationDBModel fromDB = stationRepository.findByName(insertedDBModel.getName()).get();
		assertEquals(STATION_5, fromDB.getName());
		assertEquals(CALL_SIGN_5, fromDB.getCallSign());
		assertEquals(TRUE, fromDB.getHdEnabled());
	}
	
	@Test
	public void test_findHdEnabled_Stations() {
		StationDBModel stationDBModel = new StationDBModel();
		stationDBModel.setName(STATION_1);
		stationDBModel.setHdEnabled(TRUE);
		stationDBModel.setCallSign(CALL_SIGN_1);
		stationRepository.save(stationDBModel);
		
		stationDBModel = new StationDBModel();
		stationDBModel.setName(STATION_2);
		stationDBModel.setHdEnabled(FALSE);
		stationDBModel.setCallSign(CALL_SIGN_2);
		stationRepository.save(stationDBModel);
		
		stationDBModel = new StationDBModel();
		stationDBModel.setName(STATION_3);
		stationDBModel.setHdEnabled(TRUE);
		stationDBModel.setCallSign(CALL_SIGN_3);
		stationRepository.save(stationDBModel);

		final Iterable<StationDBModel> iter = stationRepository.findByHdEnabled(Boolean.TRUE);
		final List<StationDBModel> hdEnabledStations = StreamSupport.stream(iter.spliterator(), false)
				.collect(Collectors.toList());
		assertEquals(2, hdEnabledStations.size());

	}
	
	@Test
	public void test_when_station_not_found(){
		final Optional<StationDBModel> found = stationRepository.findByStationId(UUID.randomUUID().toString());
		assertTrue(!found.isPresent());
	}
}
