package com.kk.api.station.controller;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.kk.api.station.controller.StationsApiController;
import com.kk.api.station.exception.StationApiError;
import com.kk.api.station.exception.StationApiException;
import com.kk.api.station.model.Station;
import com.kk.api.station.repository.StationRepository;
import com.kk.api.station.service.StationService;
import com.kk.api.station.transform.StationTransformer;

@WebMvcTest(controllers = StationsApiController.class)
public class StationsApiControllerTest {
	
	private static final String ADD_STATION_JSON = "{  \"name\" : \"station-101\",  \"callSign\" : \"callSign-101\",  \"stationId\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",  \"hdEnabled\" : true}";
	private static final String UPDATE_STATION_JSON = "{  \"name\" : \"station-101\",  \"callSign\" : \"callSign-101\",  \"stationId\" : \"4cbc3d3c-c29f-4332-92a3-11451710da28\",  \"hdEnabled\" : false}";
	private static final String DELETE_STATION_JSON = "{  \"name\" : \"station-101\",  \"callSign\" : \"callSign-101\",  \"stationId\" : \"4cbc3d3c-c29f-4332-92a3-11451710da28\",  \"hdEnabled\" : false}";
	
	private static final String ADD_URI = "/stations";
	private static final String UPDATE_URI = "/stations/{stationId}";
	private static final String DELETE_URI = "/stations/{stationId}";
	private static final String LIST_URI = "/stations";
	private static final String FIND_STATION_BY_ID_URI = "/stations/{stationId}";
	private static final String FIND_STATION_BY_NAME_URI = "/stations/findByName";
	private static final String FIND_HD_ENABLED_STATIONS_URI = "/stations/findHdEnabled";
	
	private static final String PARAM_NAME = "name";
	
	private final UUID STATION_ID_1 = UUID.fromString("4cbc3d3c-c29f-4332-92a3-11451710da28");
	private final UUID STATION_ID_2 = UUID.fromString("4cbc3d3c-c29f-4332-92a3-11451710da28");
	private final UUID STATION_ID_3 = UUID.fromString("4cbc3d3c-c29f-4332-92a3-11451710da28");
	
	private static final String STATION_1 = "STATION_1";
	private static final String STATION_2 = "STATION_2";
	private static final String STATION_3 = "STATION_3";
	
	private static final String CALL_SIGN_1 = "CALL_SIGN_1";
	private static final String CALL_SIGN_2 = "CALL_SIGN_2";
	private static final String CALL_SIGN_3 = "CALL_SIGN_3";
	
	private static final Boolean TRUE = Boolean.TRUE;
	private static final Boolean FALSE = Boolean.FALSE;

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Mock
	private StationService stationService;
	
	@Mock
	private StationRepository stationRepository;
	
	@Mock
	private StationTransformer stationTransformer;

	@InjectMocks
	private StationsApiController stationsApiController;

	private MockMvc mockMvc;

	@Before
	public void setup() throws Exception {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(stationsApiController).build();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testAddStation_201() throws Exception {
		final String newStationId = UUID.randomUUID().toString();
		when(stationService.addStation(any(Station.class))).thenReturn(newStationId);
		
		mockMvc.perform(post(ADD_URI).content(ADD_STATION_JSON).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
		.andDo(print())
		.andExpect(status().isCreated())
		.andExpect(header().string("location",notNullValue()));
		
		verify(stationService).addStation(any(Station.class));
	}
	
	@Test
	public void testAddStation_409() throws Exception {
		when(stationService.addStation(any(Station.class)))
			.thenThrow(new StationApiException(StationApiError.ALREADY_EXISTS,StationApiException.StationApiResponseCode.CONFLICT));
		
		mockMvc.perform(post(ADD_URI).content(ADD_STATION_JSON).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
		.andDo(print())
		.andExpect(status().isConflict());
		
		verify(stationService).addStation(any(Station.class));
	}
	
	@Test
	public void testAddStation_415() throws Exception {
		mockMvc.perform(post(ADD_URI).content(ADD_STATION_JSON).contentType(MediaType.APPLICATION_XML))
		.andDo(print())
		.andExpect(status().isUnsupportedMediaType());
	}
	
	@Test
	public void testAddStation_500() throws Exception {
		when(stationService.addStation(any(Station.class))).thenThrow(new RuntimeException());
		
		mockMvc.perform(post(ADD_URI).content(ADD_STATION_JSON).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
		.andDo(print())
		.andExpect(status().isInternalServerError());
		
		verify(stationService).addStation(any(Station.class));
	}

	@Test
	public void testUpdateStation_200() throws Exception {
		final Station station = new Station();
		station.setStationId(STATION_ID_1);
		station.setCallSign(CALL_SIGN_1);
		station.setName(STATION_1);
		station.setHdEnabled(FALSE);
		
		when(stationService.updateStation(any(UUID.class),any(Station.class))).thenReturn(station);
		
		mockMvc.perform(put(UPDATE_URI,STATION_ID_1.toString()).content(UPDATE_STATION_JSON).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
		.andDo(print())
		.andExpect(status().is2xxSuccessful())
		.andExpect(jsonPath("$.stationId").value(STATION_ID_1.toString()))
		.andExpect(jsonPath("$.name").value(STATION_1))
		.andExpect(jsonPath("$.callSign").value(CALL_SIGN_1))
		.andExpect(jsonPath("$.hdEnabled").value(FALSE));
		
		verify(stationService).updateStation(any(UUID.class),any(Station.class));
	}
	
	@Test
	public void testUpdateStation_404() throws Exception {
		when(stationService.updateStation(any(UUID.class),any(Station.class)))
		.thenThrow(new StationApiException(StationApiError.NOT_FOUND,StationApiException.StationApiResponseCode.NOT_FOUND));
		
		mockMvc.perform(put(UPDATE_URI,STATION_ID_1.toString()).content(UPDATE_STATION_JSON).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
		.andDo(print())
		.andExpect(status().isNotFound());
		
		verify(stationService).updateStation(any(UUID.class),any(Station.class));
	}
	
	@Test
	public void testUpdateStation_415() throws Exception {
		mockMvc.perform(put(UPDATE_URI,STATION_ID_1.toString()).content(UPDATE_STATION_JSON).contentType(MediaType.APPLICATION_XML))
		.andDo(print())
		.andExpect(status().isUnsupportedMediaType());
	}
	
	@Test
	public void testUpdateStation_500() throws Exception {
		when(stationService.updateStation(any(UUID.class),any(Station.class))).thenThrow(new RuntimeException());
		
		mockMvc.perform(put(UPDATE_URI,STATION_ID_1.toString()).content(UPDATE_STATION_JSON).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
		.andDo(print())
		.andExpect(status().isInternalServerError());
		
		verify(stationService).updateStation(any(UUID.class),any(Station.class));
	}
	
	@Test
	public void testDeleteStation_200() throws Exception {
		doNothing().when(stationService).deleteStation(any(UUID.class),any(Station.class));
		
		mockMvc.perform(delete(DELETE_URI,STATION_ID_1.toString()).content(DELETE_STATION_JSON).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andDo(print())
			.andExpect(status().isNoContent());
		
		verify(stationService).deleteStation(any(UUID.class),any(Station.class));
	}

	@Test
	public void testDeleteStation_404() throws Exception {
		doThrow(new StationApiException(StationApiError.NOT_FOUND,StationApiException.StationApiResponseCode.NOT_FOUND))
			.when(stationService).deleteStation(any(UUID.class),any(Station.class));
		
		mockMvc.perform(delete(DELETE_URI,STATION_ID_1.toString()).content(DELETE_STATION_JSON).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andDo(print())
			.andExpect(status().isNotFound());
		
		verify(stationService).deleteStation(any(UUID.class),any(Station.class));
	}
	
	@Test
	public void testDeleteStation_500() throws Exception {
		doThrow(new RuntimeException()).when(stationService).deleteStation(any(UUID.class),any(Station.class));
		
		mockMvc.perform(delete(DELETE_URI,STATION_ID_1.toString()).content(DELETE_STATION_JSON).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andDo(print())
			.andExpect(status().isInternalServerError());
		
		verify(stationService).deleteStation(any(UUID.class),any(Station.class));
	}
	
	@Test
	public void testFindStationById_200() throws Exception {
		final Station station = getStation();
		when(stationService.findByStationId(any(UUID.class))).thenReturn(station);
		
		mockMvc.perform(get(FIND_STATION_BY_ID_URI,STATION_ID_1.toString()).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andDo(print())
			.andExpect(status().is2xxSuccessful())
			.andExpect(jsonPath("$.stationId").value(STATION_ID_1.toString()))
			.andExpect(jsonPath("$.name").value(STATION_1))
			.andExpect(jsonPath("$.callSign").value(CALL_SIGN_1))
			.andExpect(jsonPath("$.hdEnabled").value(TRUE));
		
		verify(stationService).findByStationId(any(UUID.class));
	}
	
	@Test
	public void testFindStationById_404() throws Exception {
		when(stationService.findByStationId(any(UUID.class)))
			.thenThrow(new StationApiException(StationApiError.NOT_FOUND,StationApiException.StationApiResponseCode.NOT_FOUND));
		
		mockMvc.perform(get(FIND_STATION_BY_ID_URI,STATION_ID_1.toString()).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andDo(print())
			.andExpect(status().isNotFound());
		
		verify(stationService).findByStationId(any(UUID.class));
	}
	
	@Test
	public void testFindStationById_500() throws Exception {
		when(stationService.findByStationId(any(UUID.class))).thenThrow(new RuntimeException());
		
		mockMvc.perform(get(FIND_STATION_BY_ID_URI,STATION_ID_1.toString()).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andDo(print())
			.andExpect(status().isInternalServerError());
		
		verify(stationService).findByStationId(any(UUID.class));
	}
	
	@Test
	public void testFindStationByName_200() throws Exception {
		final Station station = getStation();
		when(stationService.findByStationName(any(String.class))).thenReturn(station);
		
		mockMvc.perform(get(FIND_STATION_BY_NAME_URI).param(PARAM_NAME, STATION_1).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andDo(print())
			.andExpect(status().is2xxSuccessful())
			.andExpect(jsonPath("$.stationId").value(STATION_ID_1.toString()))
			.andExpect(jsonPath("$.name").value(STATION_1))
			.andExpect(jsonPath("$.callSign").value(CALL_SIGN_1))
			.andExpect(jsonPath("$.hdEnabled").value(TRUE));
		
		verify(stationService).findByStationName(any(String.class));
	}
	
	@Test
	public void testFindStationByName_404() throws Exception {
		when(stationService.findByStationName(any(String.class)))
			.thenThrow(new StationApiException(StationApiError.NOT_FOUND,StationApiException.StationApiResponseCode.NOT_FOUND));
	
		mockMvc.perform(get(FIND_STATION_BY_NAME_URI).param(PARAM_NAME, STATION_2).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andDo(print())
			.andExpect(status().isNotFound());
	
		verify(stationService).findByStationName(any(String.class));
	}
	
	@Test
	public void testFindStationByName_500() throws Exception {
		when(stationService.findByStationName(any(String.class))).thenThrow(new RuntimeException());
		
		mockMvc.perform(get(FIND_STATION_BY_NAME_URI).param(PARAM_NAME, STATION_2).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andDo(print())
			.andExpect(status().isInternalServerError());
	}
	
	@Test
	public void testFindHdStations_200() throws Exception {
		final List<Station> stations = getStations();
		when(stationService.findHdEnabledStations()).thenReturn(stations);
		
		mockMvc.perform(get(FIND_HD_ENABLED_STATIONS_URI).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andDo(print())
			.andExpect(status().is2xxSuccessful())
			.andExpect(jsonPath("$", hasSize(3)))
			.andExpect(jsonPath("$[0].stationId").value(STATION_ID_1.toString()))
			.andExpect(jsonPath("$[0].name").value(STATION_1))
			.andExpect(jsonPath("$[0].callSign").value(CALL_SIGN_1))
			.andExpect(jsonPath("$[0].hdEnabled").value(TRUE))
			
			.andExpect(jsonPath("$[1].stationId").value(STATION_ID_2.toString()))
			.andExpect(jsonPath("$[1].name").value(STATION_2))
			.andExpect(jsonPath("$[1].callSign").value(CALL_SIGN_2))
			.andExpect(jsonPath("$[1].hdEnabled").value(TRUE))
			
			.andExpect(jsonPath("$[2].stationId").value(STATION_ID_3.toString()))
			.andExpect(jsonPath("$[2].name").value(STATION_3))
			.andExpect(jsonPath("$[2].callSign").value(CALL_SIGN_3))
			.andExpect(jsonPath("$[2].hdEnabled").value(TRUE));
			 
		verify(stationService).findHdEnabledStations();
	}
	
	@Test
	public void testFindHdStations_500() throws Exception {
		when(stationService.findHdEnabledStations()).thenThrow(new RuntimeException());
		
		mockMvc.perform(get(FIND_HD_ENABLED_STATIONS_URI).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andDo(print())
			.andExpect(status().isInternalServerError());
	}
	
	@Test
	public void testListAllStations_200() throws Exception {
		final List<Station> stations = getStations();
		when(stationService.listAllStations()).thenReturn(stations);
		
		mockMvc.perform(get(LIST_URI).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andDo(print())
			.andExpect(status().is2xxSuccessful())
			.andExpect(jsonPath("$", hasSize(3)))
			.andExpect(jsonPath("$[0].stationId").value(STATION_ID_1.toString()))
			.andExpect(jsonPath("$[0].name").value(STATION_1))
			.andExpect(jsonPath("$[0].callSign").value(CALL_SIGN_1))
			.andExpect(jsonPath("$[0].hdEnabled").value(TRUE))
			
			.andExpect(jsonPath("$[1].stationId").value(STATION_ID_2.toString()))
			.andExpect(jsonPath("$[1].name").value(STATION_2))
			.andExpect(jsonPath("$[1].callSign").value(CALL_SIGN_2))
			.andExpect(jsonPath("$[1].hdEnabled").value(TRUE))
			
			.andExpect(jsonPath("$[2].stationId").value(STATION_ID_3.toString()))
			.andExpect(jsonPath("$[2].name").value(STATION_3))
			.andExpect(jsonPath("$[2].callSign").value(CALL_SIGN_3))
			.andExpect(jsonPath("$[2].hdEnabled").value(TRUE));
			 
		verify(stationService).listAllStations();
	}
	
	@Test
	public void testListAllStations_500() throws Exception {
		when(stationService.listAllStations()).thenThrow(new RuntimeException());
		
		mockMvc.perform(get(LIST_URI).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andDo(print())
			.andExpect(status().isInternalServerError());
	}
	
	private Station getStation() {
		final Station station = new Station();
		station.setStationId(STATION_ID_1);
		station.setCallSign(CALL_SIGN_1);
		station.setName(STATION_1);
		station.setHdEnabled(TRUE);
		return station;
	}
	
	private List<Station> getStations(){
		final List<Station> stations = new ArrayList<>();
		Station station = new Station();
		station.setStationId(STATION_ID_1);
		station.setCallSign(CALL_SIGN_1);
		station.setName(STATION_1);
		station.setHdEnabled(TRUE);
		stations.add(station);
		
		station = new Station();
		station.setStationId(STATION_ID_2);
		station.setName(STATION_2);
		station.setCallSign(CALL_SIGN_2);
		station.setHdEnabled(TRUE);
		stations.add(station);
		
		station = new Station();
		station.setStationId(STATION_ID_3);
		station.setName(STATION_3);
		station.setCallSign(CALL_SIGN_3);
		station.setHdEnabled(TRUE);
		stations.add(station);
		
		return stations;
	}
	
}
