package it.smartcommunitylab.trafficestimator.service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import it.smartcommunitylab.trafficestimator.model.SelectedFlowData;
import it.smartcommunitylab.trafficestimator.model.TrafficEstimate;
import it.smartcommunitylab.trafficestimator.model.osmdata;
import it.smartcommunitylab.trafficestimator.utility.DateFormatUtility;

@Component
public class FlowService {

    @Value("${seconds.minus}")
    private Long secondstoMinus;
    
    @Autowired
    @Qualifier("trafficEntityManager")
    private EntityManager trafficEntityManager;

    public TrafficEstimate processGeometry(List<String> period, List<osmdata> osmGeomtery) throws ParseException {
        TrafficEstimate trafficEstimate = new TrafficEstimate();

        List<Double> impactList = new ArrayList<Double>();
        List<Double> confidenceList = new ArrayList<Double>();

        Timestamp now = DateFormatUtility.getCurrentTime();
        String startString = period.get(0);
        String endString = period.get(1);
        Timestamp start = DateFormatUtility.convertStringtoTimeStamp(startString.replace("T", " "));
        Long diff = Instant.parse(endString).getEpochSecond() - Instant.parse(startString).getEpochSecond();

        // for future dates look in the past
        if (now.before((start))) {
            startString = Instant.parse(startString).minusSeconds(secondstoMinus).toString();
            endString = Instant.parse(endString).minusSeconds(secondstoMinus).toString();
        }

        // do the logic for retrieving tmc id from the tmcid table calculate delta of
        // freeflow and nominal and return delta in case past timezone
        for (osmdata osmGeo : osmGeomtery) {

            // Repository jpa style access of database --starts

            List<SelectedFlowData> flowData = returnSelectedFlowdata(osmGeo.getTmc_id(), startString, endString);

            if (flowData.isEmpty()) {
                // separate condition to find data window within "known data"
                String newStartString = Instant.now().minusSeconds(secondstoMinus).toString();
                String newEndString = Instant.now().minusSeconds(secondstoMinus + diff).toString();
                flowData = returnSelectedFlowdata(osmGeo.getTmc_id(), newStartString, newEndString);
            }
            processFlowData(impactList, confidenceList, flowData);

            // Repository jpa style access of database --ends

        }
        // --currently calculating max of final impact and avg confidence scores
        Double finalImpact = impactList.stream().mapToDouble(i -> i).max().orElse(0);
        Double finalConfidence = confidenceList.stream().mapToDouble(i -> i).sum() / confidenceList.size();

        trafficEstimate.setEstimatedDelay(finalImpact);
        trafficEstimate.setConfidenceFactor(finalConfidence);

        return trafficEstimate;
    }

    private void processFlowData(List<Double> impactList, List<Double> confidenceList,
            List<SelectedFlowData> flowData) {
        Double freeFlowSpeed;
        Double nominalSpeed;
        Double confidence;
        Double impact;

        for (SelectedFlowData flowD : flowData) {
            freeFlowSpeed = Double.valueOf(flowD.getFree_flow_speed());
            nominalSpeed = Double.valueOf(flowD.getSpeed());
            confidence = Double.valueOf(flowD.getConfidence_factor());
            impact = freeFlowSpeed < nominalSpeed ? 0 : ((freeFlowSpeed - nominalSpeed) / (freeFlowSpeed));
            impactList.add(impact);
            confidenceList.add(confidence);
        }
    }

    public List<SelectedFlowData> returnSelectedFlowdata(String tmc_id, String timeSlot1, String timeSlot2) {
//       List<SelectedFlowData> flowData = flowDataRepository.findFlowDataInformationbytmcIdandtimeslot(tmc_id,
//      timeSlot1, timeSlot2);

        String query = "SELECT * from selectedflowdata s where s.tmc_id = ? AND s.timestamp BETWEEN CAST(? AS timestamp) AND CAST(? AS timestamp) ORDER BY jam_factor DESC LIMIT 3";
        Query queryFinal = trafficEntityManager.createNativeQuery(query, osmdata.class);
        queryFinal.setParameter(1, Long.parseLong(tmc_id));
        queryFinal.setParameter(2, timeSlot1);
        queryFinal.setParameter(3, timeSlot2);

        List<SelectedFlowData> flowData = queryFinal.getResultList();

        return flowData;
    }

}
