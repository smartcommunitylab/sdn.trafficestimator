package it.smartcommunitylab.trafficestimator.model;

import io.swagger.annotations.ApiModelProperty;

public class TrafficEstimate {

	public TrafficEstimate() {
		// TODO Auto-generated constructor stub
	}

	@ApiModelProperty("Average confidence factor of the estimate")
	private Double confidenceFactor;
	@ApiModelProperty("Time increment estimate (from 0 to 1)")
	private Double estimatedDelay;
	public Double getConfidenceFactor() {
		return confidenceFactor;
	}
	public void setConfidenceFactor(Double confidenceFactor) {
		this.confidenceFactor = confidenceFactor;
	}
	public Double getEstimatedDelay() {
		return estimatedDelay;
	}
	public void setEstimatedDelay(Double estimatedDelay) {
		this.estimatedDelay = estimatedDelay;
	}
	
	
	
	
}
