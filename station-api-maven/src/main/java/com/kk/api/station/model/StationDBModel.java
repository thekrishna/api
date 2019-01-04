package com.kk.api.station.model;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * 
 * @author Krishna Kumar
 *
 */
@Entity
@Table(name = "station")
public class StationDBModel {
	@Id
	private String stationId;
	private String name;
	private Boolean hdEnabled;
	private String callSign;

	public StationDBModel() {
		this.stationId= UUID.randomUUID().toString();
	}

	public String getStationId() {
		return stationId;
	}

	public void setStationId(String stationId) {
		this.stationId = stationId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getHdEnabled() {
		return hdEnabled;
	}

	public void setHdEnabled(Boolean hdEnabled) {
		this.hdEnabled = hdEnabled;
	}

	public String getCallSign() {
		return callSign;
	}

	public void setCallSign(String callSign) {
		this.callSign = callSign;
	}

	@Override
	public String toString() {
		return String.format("Station [id=%s, name=%s, hdEnabled=%s, " + "callSign=%s]", stationId, name, hdEnabled,
				callSign);
	}

}
