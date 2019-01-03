package com.kk.api.station.model;

import java.util.Objects;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * Station
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2019-01-02T02:13:32.583+05:30")

public class Station {
	@JsonProperty("stationId")
	private UUID stationId = null;

	@JsonProperty("name")
	private String name = null;

	@JsonProperty("hdEnabled")
	private Boolean hdEnabled = false;

	@JsonProperty("callSign")
	private String callSign = null;

	public Station stationId(UUID stationId) {
		this.stationId = stationId;
		return this;
	}

	/**
	 * Unique identifier for station Id
	 * 
	 * @return stationId
	 **/
	@ApiModelProperty(value = "Unique identifier for station Id")

	@Valid

	public UUID getStationId() {
		return stationId;
	}

	public void setStationId(UUID stationId) {
		this.stationId = stationId;
	}

	public Station name(String name) {
		this.name = name;
		return this;
	}

	/**
	 * Get name
	 * 
	 * @return name
	 **/
	@ApiModelProperty(value = "")

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Station hdEnabled(Boolean hdEnabled) {
		this.hdEnabled = hdEnabled;
		return this;
	}

	/**
	 * Get hdEnabled
	 * 
	 * @return hdEnabled
	 **/
	@ApiModelProperty(value = "")

	public Boolean isHdEnabled() {
		return hdEnabled;
	}

	public void setHdEnabled(Boolean hdEnabled) {
		this.hdEnabled = hdEnabled;
	}

	public Station callSign(String callSign) {
		this.callSign = callSign;
		return this;
	}

	/**
	 * Get callSign
	 * 
	 * @return callSign
	 **/
	@ApiModelProperty(value = "")

	public String getCallSign() {
		return callSign;
	}

	public void setCallSign(String callSign) {
		this.callSign = callSign;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Station station = (Station) o;
		return Objects.equals(this.stationId, station.stationId) && Objects.equals(this.name, station.name)
				&& Objects.equals(this.hdEnabled, station.hdEnabled) && Objects.equals(this.callSign, station.callSign);
	}

	@Override
	public int hashCode() {
		return Objects.hash(stationId, name, hdEnabled, callSign);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class Station {\n");

		sb.append("    stationId: ").append(toIndentedString(stationId)).append("\n");
		sb.append("    name: ").append(toIndentedString(name)).append("\n");
		sb.append("    hdEnabled: ").append(toIndentedString(hdEnabled)).append("\n");
		sb.append("    callSign: ").append(toIndentedString(callSign)).append("\n");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Convert the given object to string with each line indented by 4 spaces
	 * (except the first line).
	 */
	private String toIndentedString(java.lang.Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}
}
