package it.smartcommunitylab.trafficestimator.model;

import java.io.Serializable;
import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import it.smartcommunitylab.trafficestimator.model.TrafficData.TrafficDataPK;

@Entity
public class TrafficPredict implements JamFactorData {

    @EmbeddedId
    private TrafficPredictPK id;

    @Column(name = "jam_factor")
    private double jamFactor;

    public OffsetDateTime getTimestamp() {
        return id.timestamp;
    }

    public void setTimestamp(OffsetDateTime timestamp) {
        this.id.timestamp = timestamp;
    }

    public long getTmcId() {
        return id.tmcId;
    }

    public void setTmcId(long tmcId) {
        this.id.tmcId = tmcId;
    }

    public double getJamFactor() {
        return jamFactor;
    }

    public void setJamFactor(double jamFactor) {
        this.jamFactor = jamFactor;
    }

    public double getConfidenceFactor() {
        // return fixed value for now
        // TODO fix
        return (double) 0.0;
    }

    public TrafficPredictPK getId() {
        return id;
    }

    public void setId(TrafficPredictPK id) {
        this.id = id;
    }

    @Embeddable
    public static class TrafficPredictPK implements Serializable {

        private static final long serialVersionUID = -4652110930686270782L;

        @Column(name = "tmc_id")
        public long tmcId;

        @Column(name = "timestamp", columnDefinition = "TIMESTAMP WITH TIME ZONE")
        public OffsetDateTime timestamp;

        public long getTmcId() {
            return tmcId;
        }

        public void setTmcId(long tmcId) {
            this.tmcId = tmcId;
        }

        public OffsetDateTime getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(OffsetDateTime timestamp) {
            this.timestamp = timestamp;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
            result = prime * result + (int) (tmcId ^ (tmcId >>> 32));
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
            TrafficDataPK other = (TrafficDataPK) obj;
            if (timestamp == null) {
                if (other.timestamp != null)
                    return false;
            } else if (!timestamp.equals(other.timestamp))
                return false;
            if (tmcId != other.tmcId)
                return false;
            return true;
        }

    }

}
