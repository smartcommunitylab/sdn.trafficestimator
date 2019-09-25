package it.smartcommunitylab.trafficestimator.model;

import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class TrafficPredict implements JamFactorData {
    @Column(name = "timestamp", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime timestamp;

    @Column(name = "tmc_id")
    private long tmcId;

    @Column(name = "jam_factor")
    private double jamFactor;

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(OffsetDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public long getTmcId() {
        return tmcId;
    }

    public void setTmcId(long tmcId) {
        this.tmcId = tmcId;
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

}
