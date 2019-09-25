package it.smartcommunitylab.trafficestimator.model;

import java.time.OffsetDateTime;

public interface JamFactorData {

    public OffsetDateTime getTimestamp();

    public long getTmcId();

    public double getJamFactor();

    public double getConfidenceFactor();

}
