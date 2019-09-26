package it.smartcommunitylab.trafficestimator.model;

import java.io.Serializable;
import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class TrafficData implements JamFactorData {

    @EmbeddedId
    private TrafficDataPK id;

    @Column(name = "speed")
    private double speed;

    @Column(name = "jam_factor")
    private double jamFactor;

    @Column(name = "free_flow_speed")
    private double freeFlowSpeed;

    @Column(name = "length")
    private double length;

    @Column(name = "confidence_factor")
    private double confidenceFactor;

    public TrafficDataPK getId() {
        return id;
    }

    public void setId(TrafficDataPK id) {
        this.id = id;
    }

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

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getJamFactor() {
        return jamFactor;
    }

    public void setJamFactor(double jamFactor) {
        this.jamFactor = jamFactor;
    }

    public double getFreeFlowSpeed() {
        return freeFlowSpeed;
    }

    public void setFreeFlowSpeed(double freeFlowSpeed) {
        this.freeFlowSpeed = freeFlowSpeed;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getConfidenceFactor() {
        return confidenceFactor;
    }

    public void setConfidenceFactor(double confidenceFactor) {
        this.confidenceFactor = confidenceFactor;
    }

    @Embeddable
    public static class TrafficDataPK implements Serializable {

        private static final long serialVersionUID = -6359768634284019808L;

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
