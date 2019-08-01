package it.smartcommunitylab.trafficestimator.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;


@Embeddable
public class geometry {

	public geometry() {
		// TODO Auto-generated constructor stub
	}

	 @JsonProperty("coordinates")
	 @Column(name="coordinates")
	 @Transient
	  private  JsonNode coordinates;
	 @Column(name="type")
	 @JsonProperty("type")
	private String type;
	
}
