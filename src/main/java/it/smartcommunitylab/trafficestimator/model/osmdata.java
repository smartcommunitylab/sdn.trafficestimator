package it.smartcommunitylab.trafficestimator.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonProperty;


@Entity
@Table(name="osmdatatrento",schema="public")
public class osmdata {

	public osmdata() {
		// TODO Auto-generated constructor stub
	}


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String osm_id;
	private String tmc_id;
	@Transient
	@JsonProperty("geometry")
	private String geometry;
	
	
	
	
	public String getGeometry() {
		return geometry;
	}
	public void setGeometry(String geometry) {
		this.geometry = geometry;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getOsm_id() {
		return osm_id;
	}
	public void setOsm_id(String osm_id) {
		this.osm_id = osm_id;
	}
	public String getTmc_id() {
		return tmc_id;
	}
	public void setTmc_id(String tmc_id) {
		this.tmc_id = tmc_id;
	}
	
	
	
}
