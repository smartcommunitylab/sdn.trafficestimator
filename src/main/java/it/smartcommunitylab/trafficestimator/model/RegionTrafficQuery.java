package it.smartcommunitylab.trafficestimator.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

import io.swagger.annotations.ApiModelProperty;

@JsonTypeName(value = "RegionTrafficQuery")
//@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
public class RegionTrafficQuery {

	public RegionTrafficQuery() {
		// TODO Auto-generated constructor stub
	}

	@ApiModelProperty(value="List of latitudes of the route points", required = true)
	@JsonProperty("latitudes")
	private List<Double> latitudes;
	@ApiModelProperty(value="List of longitudes of the route points", required = true)
	@JsonProperty("longtitudes")
	private List<Double> longitudes;
	@ApiModelProperty(value="Time interval in the format of ISO 8601 format", required = true)
	@JsonProperty("period")
	private List<String> period;
	@ApiModelProperty(value="Optional reference to the region ID for speed up the queries", required = false)
	private String regionId;

	public List<Double> getLatitudes() {
		return latitudes;
	}

	public void setLatitudes(List<Double> latitudes) {
		this.latitudes = latitudes;
	}

	public List<Double> getLongitudes() {
		return longitudes;
	}

	public void setLongitudes(List<Double> longitudes) {
		this.longitudes = longitudes;
	}

	public List<String> getPeriod() {
		return period;
	}

	public void setPeriod(List<String> period) {
		this.period = period;
	}

	public String getRegionId() {
		return regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}

}
