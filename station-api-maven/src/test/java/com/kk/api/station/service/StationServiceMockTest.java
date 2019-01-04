package com.kk.api.station.service;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

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
public class StationServiceMockTest {
	
	private static final String NOT_FOUND_STATION = "STATION-123";
	private static final String STATION = "STATION-";
	private static final String CALL_SIGN = "CALL_SIGN-";
	private static final String STATION_API_ERROR ="stationApiError";
	
	private static final Boolean TRUE = Boolean.TRUE;
	private static final Boolean FALSE = Boolean.FALSE;
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	private StationService stationService;
	private StationTransformer stationTransformer;
	private StationRepository stationRepository;
	
	@Before
	public void setup() throws Exception{
		stationTransformer = mock(StationTransformer.class);
		stationRepository = mock(StationRepository.class);
		stationService = new StationService(stationRepository, stationTransformer);
	}
	
	@Test
	public void test_01_add_station_201() throws Exception{
		final UUID stationId = UUID.randomUUID();
		final Station station = getStation(1,stationId);
		final StationDBModel stationDBModel=getStationDBModel(1,stationId);
		
		when(stationRepository.findByName(any(String.class))).thenReturn(Optional.empty());
		when(stationTransformer.mapApiToDbModel(any(Station.class))).thenReturn(stationDBModel);
		when(stationRepository.save(any(StationDBModel.class))).thenReturn(stationDBModel);
		
		final String actualStationId = stationService.addStation(station);
		assertEquals(station.getStationId().toString(), actualStationId);
		
		verify(stationTransformer).mapApiToDbModel(any(Station.class));
		verify(stationRepository).findByName(any(String.class));
		verify(stationRepository).save(any(StationDBModel.class));
	}
	
	@Test
	public void test_02_add_station_409() throws Exception{
		expectedException.expect(StationApiException.class);
		expectedException.expect(hasProperty(STATION_API_ERROR,sameInstance(StationApiError.ALREADY_EXISTS)));
		
		final UUID stationId = UUID.randomUUID();
		final Station station = getStation(1,stationId);
		final StationDBModel stationDBModel = getStationDBModel(1, stationId);
		
		when(stationRepository.findByName(any(String.class))).thenReturn(Optional.of(stationDBModel));
		stationService.addStation(station);
		verify(stationRepository).findByName(any(String.class));
	}
	
	@Test
	public void test_03_add_station_500() throws Exception{
		expectedException.expect(RuntimeException.class);
		
		final UUID stationId = UUID.randomUUID();
		final Station station = getStation(3,stationId);
		
		when(stationRepository.findByName(any(String.class))).thenReturn(Optional.empty());
		when(stationTransformer.mapApiToDbModel(any(Station.class))).thenThrow(new RuntimeException());
		
		stationService.addStation(station);
		
		verify(stationRepository).findByName(any(String.class));
		verify(stationTransformer).mapApiToDbModel(any(Station.class));
	}
	
	@Test
	public void test_04_update_station_200() throws Exception{
		int index=4;
		final UUID stationId = UUID.randomUUID();
		final Station station = getStation(index,stationId);
		final StationDBModel stationDBModel=getStationDBModel(index,stationId);
		
		when(stationRepository.findByStationId(any(String.class))).thenReturn(Optional.of(stationDBModel));
		when(stationRepository.save(any(StationDBModel.class))).thenReturn(stationDBModel);
		when(stationTransformer.mapDbModelToApi(any(StationDBModel.class))).thenReturn(station);
		
		final Station actualStation = stationService.updateStation(stationId, station);
		assertEquals(stationId, actualStation.getStationId());
		assertEquals(station.getName(), actualStation.getName());
		assertEquals(station.getCallSign(), actualStation.getCallSign());
		assertEquals(station.isHdEnabled(), actualStation.isHdEnabled());
		
		verify(stationRepository).findByStationId(any(String.class));
		verify(stationRepository).save(any(StationDBModel.class));
		verify(stationTransformer).mapDbModelToApi(any(StationDBModel.class));
	}
	
	@Test
	public void test_05_update_station_404() throws Exception{
		expectedException.expect(StationApiException.class);
		expectedException.expect(hasProperty(STATION_API_ERROR,sameInstance(StationApiError.NOT_FOUND)));
		
		final UUID stationId = UUID.randomUUID();
		final Station station = getStation(5,stationId);
		
		when(stationRepository.findByStationId(any(String.class))).thenReturn(Optional.empty());
		stationService.updateStation(stationId, station);
		verify(stationRepository).findByStationId(any(String.class));
	}
	
	@Test
	public void test_06_update_station_500() throws Exception{
		expectedException.expect(RuntimeException.class);
		
		int index=6;
		final UUID stationId = UUID.randomUUID();
		final Station station = getStation(index,stationId);
		final StationDBModel stationDBModel = getStationDBModel(index, stationId);
		
		when(stationRepository.findByStationId(any(String.class))).thenReturn(Optional.of(stationDBModel));
		when(stationRepository.save(any(StationDBModel.class))).thenReturn(stationDBModel);
		when(stationTransformer.mapDbModelToApi(any(StationDBModel.class))).thenThrow(new RuntimeException());
		
		stationService.updateStation(stationId, station);
		
		verify(stationRepository).findByStationId(any(String.class));
		verify(stationRepository).save(any(StationDBModel.class));
		verify(stationTransformer).mapDbModelToApi(any(StationDBModel.class));
	}
	
	@Test
	public void test_07_delete_station_200() throws Exception{
		int index=7;
		final UUID stationId = UUID.randomUUID();
		final Station station = getStation(index,stationId);
		final StationDBModel stationDBModel = getStationDBModel(index, stationId);
		
		when(stationRepository.findByStationId(any(String.class))).thenReturn(Optional.of(stationDBModel));
		when(stationRepository.save(any(StationDBModel.class))).thenReturn(stationDBModel);
		
		stationService.deleteStation(stationId, station);
		
		verify(stationRepository).findByStationId(any(String.class));
		verify(stationRepository).save(any(StationDBModel.class));
	}
	
	@Test
	public void test_08_delete_station_404() throws Exception{
		expectedException.expect(StationApiException.class);
		expectedException.expect(hasProperty(STATION_API_ERROR,sameInstance(StationApiError.NOT_FOUND)));
		
		int index=8;
		final UUID stationId = UUID.randomUUID();
		final Station station = getStation(index,stationId);
		
		when(stationRepository.findByStationId(any(String.class))).thenReturn(Optional.empty());
		stationService.deleteStation(stationId, station);
		verify(stationRepository).findByStationId(any(String.class));
	}
	
	@Test
	public void test_09_delete_station_500() throws Exception{
		expectedException.expect(RuntimeException.class);
		
		int index=9;
		final UUID stationId = UUID.randomUUID();
		final Station station = getStation(index,stationId);
		final StationDBModel stationDBModel = getStationDBModel(index, stationId);
		
		when(stationRepository.findByStationId(any(String.class))).thenReturn(Optional.of(stationDBModel));
		when(stationRepository.save(any(StationDBModel.class))).thenThrow(new RuntimeException());
		
		stationService.deleteStation(stationId, station);
		verify(stationRepository).findByStationId(any(String.class));
		verify(stationRepository).save(any(StationDBModel.class));
	}
	
	@Test
	public void test_10_findByStationId_200() throws Exception{
		int index=10;
		final UUID stationId = UUID.randomUUID();
		final Station station = getStation(index,stationId);
		final StationDBModel stationDBModel = getStationDBModel(index, stationId);
		
		when(stationRepository.findByStationId(any(String.class))).thenReturn(Optional.of(stationDBModel));
		when(stationTransformer.mapDbModelToApi(any(StationDBModel.class))).thenReturn(station);
		
		final Station actualStation=stationService.findByStationId(stationId);
		assertEquals(stationId, actualStation.getStationId());
		assertEquals(station.getName(), actualStation.getName());
		assertEquals(station.getCallSign(), actualStation.getCallSign());
		assertEquals(station.isHdEnabled(), actualStation.isHdEnabled());
		
		verify(stationRepository).findByStationId(any(String.class));
		verify(stationTransformer).mapDbModelToApi(any(StationDBModel.class));
	}
	
	@Test
	public void test_11_findByStationId_404() throws Exception{
		expectedException.expect(StationApiException.class);
		expectedException.expect(hasProperty(STATION_API_ERROR,sameInstance(StationApiError.NOT_FOUND)));
		
		final UUID stationId = UUID.randomUUID();
		
		when(stationRepository.findByStationId(any(String.class))).thenReturn(Optional.empty());
		stationService.findByStationId(stationId);
		verify(stationRepository).findByStationId(any(String.class));
	}
	
	@Test
	public void test_12_findByStationId_500() throws Exception{
		expectedException.expect(RuntimeException.class);
		
		int index=12;
		final UUID stationId = UUID.randomUUID();
		final StationDBModel stationDBModel = getStationDBModel(index, stationId);
		
		when(stationRepository.findByStationId(any(String.class))).thenReturn(Optional.of(stationDBModel));
		when(stationTransformer.mapDbModelToApi(any(StationDBModel.class))).thenThrow(new RuntimeException());
		
		stationService.findByStationId(stationId);
		
		verify(stationRepository).findByStationId(any(String.class));
		verify(stationTransformer).mapDbModelToApi(any(StationDBModel.class));
	}
	
	@Test
	public void test_13_findByStationName_200() throws Exception{
		int index=13;
		final UUID stationId = UUID.randomUUID();
		final Station station = getStation(index,stationId);
		final StationDBModel stationDBModel = getStationDBModel(index, stationId);
		
		when(stationRepository.findByName(any(String.class))).thenReturn(Optional.of(stationDBModel));
		when(stationTransformer.mapDbModelToApi(any(StationDBModel.class))).thenReturn(station);
		
		final Station actualStation=stationService.findByStationName(station.getName());
		
		assertEquals(stationId, actualStation.getStationId());
		assertEquals(station.getName(), actualStation.getName());
		assertEquals(station.getCallSign(), actualStation.getCallSign());
		assertEquals(station.isHdEnabled(), actualStation.isHdEnabled());
		
		verify(stationRepository).findByName(any(String.class));
		verify(stationTransformer).mapDbModelToApi(any(StationDBModel.class));
	}
	
	@Test
	public void test_14_findByStationName_404() throws Exception{
		expectedException.expect(StationApiException.class);
		expectedException.expect(hasProperty(STATION_API_ERROR,sameInstance(StationApiError.NOT_FOUND)));
		
		when(stationRepository.findByName(any(String.class))).thenReturn(Optional.empty());
		stationService.findByStationName(NOT_FOUND_STATION);
		verify(stationRepository).findByName(any(String.class));
	}
	
	@Test
	public void test_15_findByStationName_500() throws Exception{
		expectedException.expect(RuntimeException.class);
		
		int index=15;
		final UUID stationId = UUID.randomUUID();
		final Station station = getStation(index,stationId);
		final StationDBModel stationDBModel = getStationDBModel(index, stationId);
		
		when(stationRepository.findByName(any(String.class))).thenReturn(Optional.of(stationDBModel));
		when(stationTransformer.mapDbModelToApi(any(StationDBModel.class))).thenThrow(new RuntimeException());
		
		stationService.findByStationName(station.getName());
		
		verify(stationRepository).findByName(any(String.class));
		verify(stationTransformer).mapDbModelToApi(any(StationDBModel.class));
	}
	
	@Test
	public void test_16_findHdEnabledStations() throws Exception{
		final Iterable<StationDBModel> iterable = Collections.emptyList();
		
		when(stationRepository.findByHdEnabled(true)).thenReturn(iterable);
		
		assertEquals(Collections.emptyList(),stationService.findHdEnabledStations());
		
		verify(stationRepository).findByHdEnabled(true);
	}
	
	@Test
	public void test_17_findListAllStations() throws Exception{
		final List<StationDBModel> listToReturn = Collections.emptyList();
		
		when(stationRepository.findAll()).thenReturn(listToReturn);
		
		assertEquals(Collections.emptyList(),stationService.listAllStations());
		
		verify(stationRepository).findAll();
	}
	
	private Station getStation(int index,final UUID uuid) {
		final Station station = new Station();
		station.setStationId(uuid);
		station.setCallSign(CALL_SIGN + index);
		station.setName(STATION + index);
		station.setHdEnabled((index % 2 == 0) ? FALSE : TRUE);
		return station;
	}
	
	private StationDBModel getStationDBModel(final int index,final UUID uuid) {
		final StationDBModel stationDBModel = new StationDBModel();
		stationDBModel.setStationId(uuid.toString());
		stationDBModel.setCallSign(CALL_SIGN + index);
		stationDBModel.setName(STATION + index);
		stationDBModel.setHdEnabled((index % 2 == 0) ? FALSE : TRUE);
		return stationDBModel;
	}
	
}
