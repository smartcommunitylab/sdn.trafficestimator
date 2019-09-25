package it.smartcommunitylab.trafficestimator.service;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import it.smartcommunitylab.trafficestimator.model.JamFactorData;
import it.smartcommunitylab.trafficestimator.model.TrafficData;
import it.smartcommunitylab.trafficestimator.model.TrafficEstimate;
import it.smartcommunitylab.trafficestimator.model.TrafficPredict;
import it.smartcommunitylab.trafficestimator.model.osmdata;

@Component
public class TrafficService {

    @Autowired
    @Qualifier("trafficNewEntityManager")
    private EntityManager trafficNewEntityManager;

    /*
     * Data processing
     */
    public TrafficEstimate processGeometry(List<osmdata> geometries, String timeFrom, String timeTo) throws Exception {

        // init
        TrafficEstimate trafficEstimate = new TrafficEstimate();
        List<Double> impactList = new ArrayList<Double>();
        List<Double> confidenceList = new ArrayList<Double>();

        // parse tmcIds as set since multiple osmId could map to the same tmcId
        Set<String> tmcIds = new HashSet<>();
        for (osmdata od : geometries) {
            tmcIds.add(od.getTmc_id());
        }

        // parse dates
        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime future = now.plusMinutes(30); // now + 30m since predictions cover this range
        OffsetDateTime from = parseDateTime(timeFrom);
        OffsetDateTime to = parseDateTime(timeTo);

        for (String tmcId : tmcIds) {
            List<JamFactorData> trafficData = Collections.EMPTY_LIST;
            List<Double> iList = new ArrayList<Double>();
            List<Double> cList = new ArrayList<Double>();

            // check cases
            if (from.isBefore(now) && to.isBefore(now)) {
                // retrieve past data
                List<TrafficData> td = returnTrafficData(tmcId, from, to);
                trafficData.addAll(td);

            } else if (from.isBefore(now) && to.isAfter(now)) {
                // split now
                List<TrafficData> td = returnTrafficData(tmcId, from, now);
                trafficData.addAll(td);

                List<TrafficPredict> pd = returnTrafficPredict(tmcId, now, future);
                trafficData.addAll(pd);

            } else {
                // both in the future, bound from now to future since we don't have more data
                List<TrafficPredict> pd = returnTrafficPredict(tmcId, now, future);
                trafficData.addAll(pd);
            }
            // TODO add future interval after prediction

            // process data

            for (JamFactorData jd : trafficData) {
                double jamFactor = jd.getJamFactor();
                double confidence = jd.getConfidenceFactor();

                // map jamFactor to impact
                double impact = calculateImpact(jamFactor);

                // add to measures
                iList.add(impact);
                cList.add(confidence);
            }

            // derive final measure for the tmc
            Double fImpact = iList.stream().mapToDouble(i -> i).max().orElse(0);
            Double fConfidence = cList.stream().mapToDouble(i -> i).sum() / confidenceList.size();

            // add to resultset
            impactList.add(fImpact);
            confidenceList.add(fConfidence);
        }

        // --currently calculating max of final impact and avg confidence scores
        Double finalImpact = impactList.stream().mapToDouble(i -> i).max().orElse(0);
        Double finalConfidence = confidenceList.stream().mapToDouble(i -> i).sum() / confidenceList.size();

        trafficEstimate.setEstimatedDelay(finalImpact);
        trafficEstimate.setConfidenceFactor(finalConfidence);

        return trafficEstimate;
    }

    /*
     * Data access
     */
    public List<TrafficData> returnTrafficData(String tmcId, OffsetDateTime timeFrom, OffsetDateTime timeTo) {

        String query = "SELECT * from traffic_here_filtered s where s.tmc_id = ? AND s.timestamp BETWEEN CAST(? AS timestamp) AND CAST(? AS timestamp) ORDER BY jam_factor DESC LIMIT 3";
        Query queryFinal = trafficNewEntityManager.createNativeQuery(query, osmdata.class);
        queryFinal.setParameter(1, Long.parseLong(tmcId));
        queryFinal.setParameter(2, timeFrom.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        queryFinal.setParameter(3, timeTo.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));

        List<TrafficData> trafficData = queryFinal.getResultList();

        return trafficData;
    }

    public List<TrafficPredict> returnTrafficPredict(String tmcId, OffsetDateTime timeFrom, OffsetDateTime timeTo) {

        String query = "SELECT * from traffic_here_predict s where s.tmc_id = ? AND s.timestamp BETWEEN CAST(? AS timestamp) AND CAST(? AS timestamp) ORDER BY jam_factor DESC LIMIT 6";
        Query queryFinal = trafficNewEntityManager.createNativeQuery(query, osmdata.class);
        queryFinal.setParameter(1, Long.parseLong(tmcId));
        queryFinal.setParameter(2, timeFrom.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        queryFinal.setParameter(3, timeTo.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));

        List<TrafficPredict> predictData = queryFinal.getResultList();

        return predictData;
    }

    /*
     * Helpers
     */
    private OffsetDateTime parseDateTime(String value) {
        // Parsing ISO offset date time
        OffsetDateTime dateTime = OffsetDateTime.parse(value, DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        return dateTime;
    }

    private double calculateImpact(double jamFactor) {
        if (jamFactor < 7) {
            return jamFactor * 0.07;
        } else if (jamFactor >= 7 && jamFactor <= 9.9) {
            return jamFactor * 0.09;
        } else {
            return 2.0;
        }
    }

}
