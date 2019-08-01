package it.smartcommunitylab.trafficestimator.model;



import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;



@Entity
@Table(name="selectedflowdata",schema="public")
public class SelectedFlowData{

	@EmbeddedId
	private FlowDataPK id;
	
	private String length;
	@Column(name="jam_factor")
	private String jam_Factor;
	private String speed;
	@Column(name="free_flow_speed")
	private String free_flow_speed;
	@Column(name="confidence_factor")
	private String confidence_factor;

	public String getLength() {
		return length;
	}
	public void setLength(String length) {
		this.length = length;
	}
	
	public String getSpeed() {
		return speed;
	}
	public void setSpeed(String speed) {
		this.speed = speed;
	}
	
	
	public String getTimestamp() {
		return id.getTimestamp();
	}
	public void setTimestamp(String timestamp) {
		this.id.setTimestamp(timestamp);
	}
	public String getTmc_id() {
		return id.getTmc_id();
	}
	public void setTmc_id(String tmc_id) {
		this.id.setTmc_id(tmc_id);
	}
	public String getJam_Factor() {
		return jam_Factor;
	}
	public void setJam_Factor(String jam_Factor) {
		this.jam_Factor = jam_Factor;
	}
	public String getFree_flow_speed() {
		return free_flow_speed;
	}
	public void setFree_flow_speed(String free_flow_speed) {
		this.free_flow_speed = free_flow_speed;
	}
	public String getConfidence_factor() {
		return confidence_factor;
	}
	public void setConfidence_factor(String confidence_factor) {
		this.confidence_factor = confidence_factor;
	}
	
	
	
	
	@Embeddable
	public static class FlowDataPK implements Serializable {
		private static final long serialVersionUID = 8008817881909729843L;

		@Column(name="tmc_id")
		private String tmc_id;

	    @Column(name = "timestamp")
	    private String timestamp;

		public FlowDataPK() {
			super();
		}

		public String getTmc_id() {
			return tmc_id;
		}

		public void setTmc_id(String tmc_id) {
			this.tmc_id = tmc_id;
		}

		public String getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(String timestamp) {
			this.timestamp = timestamp;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
			result = prime * result + ((tmc_id == null) ? 0 : tmc_id.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			FlowDataPK other = (FlowDataPK) obj;
			if (timestamp == null) {
				if (other.timestamp != null)
					return false;
			} else if (!timestamp.equals(other.timestamp))
				return false;
			if (tmc_id == null) {
				if (other.tmc_id != null)
					return false;
			} else if (!tmc_id.equals(other.tmc_id))
				return false;
			return true;
		}

	 
	}




	public FlowDataPK getId() {
		return id;
	}
	public void setId(FlowDataPK id) {
		this.id = id;
	}
	
	
	
	
}
