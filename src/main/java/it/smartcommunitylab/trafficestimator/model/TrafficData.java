package it.smartcommunitylab.trafficestimator.model;

import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class TrafficData implements JamFactorData {

    @Column(name = "timestamp", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime timestamp;

    @Column(name = "tmc_id")
    private long tmcId;

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

}
