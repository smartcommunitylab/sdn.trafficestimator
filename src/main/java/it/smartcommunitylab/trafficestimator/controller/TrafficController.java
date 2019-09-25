package it.smartcommunitylab.trafficestimator.controller;

import java.sql.Timestamp;
import java.text.ParseException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import io.swagger.annotations.ApiOperation;
import it.smartcommunitylab.trafficestimator.model.RegionTrafficQuery;
import it.smartcommunitylab.trafficestimator.model.RouteTrafficQuery;
import it.smartcommunitylab.trafficestimator.model.SelectedFlowData;
import it.smartcommunitylab.trafficestimator.model.TrafficData;
import it.smartcommunitylab.trafficestimator.model.TrafficEstimate;
import it.smartcommunitylab.trafficestimator.model.TrafficPredict;
import it.smartcommunitylab.trafficestimator.model.osmdata;
import it.smartcommunitylab.trafficestimator.service.FlowService;
import it.smartcommunitylab.trafficestimator.service.OSMService;
import it.smartcommunitylab.trafficestimator.service.TrafficService;
//import it.smartcommunitylab.trafficestimator.repository.FlowDataRepository;
//import it.smartcommunitylab.trafficestimator.repository.OSMRepository;
import it.smartcommunitylab.trafficestimator.utility.DateFormatUtility;

@Controller
@Transactional
public class TrafficController {

//	@Autowired
//	OSMRepository osmRepository;
//	@Autowired
//	FlowDataRepository flowDataRepository;

//	@Value("${seconds.minus}")
//	private Long secondstoMinus;

//	@PersistenceContext
//	private EntityManager em;

//    @Autowired
//    @Qualifier("trafficNewEntityManager")
//    private EntityManager trafficNewEntityManager;
//
//    @Autowired
//    @Qualifier("trafficEntityManager")
//    private EntityManager trafficEntityManager;

//	@Autowired
//	JdbcTemplate jdbcTemplate;

//	// http://localhost:8080/osm?filter=geography'POINT ((46.4621 11.2431))'))
//	@RequestMapping(method = RequestMethod.GET, value = "/osm")
//	@ResponseBody
//	public List<osmdata> findclosestGeometry(@RequestParam(value = "filter") String filter) {
//		String newFilter = "";
//
//		int firstIndex = filter.indexOf("'");
//		if (filter.contains("POINT")) {
//			newFilter = filter.substring(firstIndex + 1, filter.indexOf("'", firstIndex + 1));
//		}
//		return osmRepository.findDistancebyGeometry(newFilter, 4326);
//
//	}

    @Autowired
    private TrafficService trafficService;

    @Autowired
    private OSMService osmService;

    @Autowired
    private FlowService flowService;

    @SuppressWarnings("unchecked")
    @ApiOperation(value = "Get OSM plus TMC data for the specified route")
    @RequestMapping(method = RequestMethod.POST, value = "/traffic/route")
    public ResponseEntity<List<osmdata>> getRouteData(@RequestBody RouteTrafficQuery query) throws Exception {

        // fetch points from query
        List<Pair<Double, Double>> points = new ArrayList<>();

        if (query.getLatitudes() != null && query.getLongitudes() != null
                && query.getLatitudes().size() == query.getLongitudes().size()) {
            for (int i = 0; i < query.getLatitudes().size(); i++) {
                points.add(Pair.of(query.getLongitudes().get(i), query.getLatitudes().get(i)));
            }
        }

        if (points.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        List<osmdata> results = osmService.getDataFromRoute(points, 10);

        return ResponseEntity.status(HttpStatus.OK).body(results);

    }
    
    

    @SuppressWarnings("unchecked")
    @ApiOperation(value = "Make a traffic query for the specified route")
    @RequestMapping(method = RequestMethod.POST, value = "/traffic/route")
    public ResponseEntity<TrafficEstimate> getRouteTraffic(@RequestBody RouteTrafficQuery query) throws Exception {
        
        TrafficEstimate trafficEstimate = null;

        // fetch points from query
        List<Pair<Double, Double>> points = new ArrayList<>();

        if (query.getLatitudes() != null && query.getLongitudes() != null
                && query.getLatitudes().size() == query.getLongitudes().size()) {
            for (int i = 0; i < query.getLatitudes().size(); i++) {
                points.add(Pair.of(query.getLongitudes().get(i), query.getLatitudes().get(i)));
            }
        }

        if (points.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        List<osmdata> geometries = osmService.getDataFromRoute(points, 10);
        trafficEstimate = trafficService.processGeometry(geometries, query.getPeriod().get(0), query.getPeriod().get(1));        

        return ResponseEntity.status(HttpStatus.OK).body(trafficEstimate);

    }
    
    @ApiOperation(value = "Make a traffic query for the specified region")
    @RequestMapping(method = RequestMethod.POST, value = "/traffic/region")
    public ResponseEntity<TrafficEstimate> getRegionTraffic(@RequestBody RegionTrafficQuery query) throws Exception {

        TrafficEstimate trafficEstimate = null;

        // fetch points from query
        List<Pair<Double, Double>> points = new ArrayList<>();

        if (query.getLatitudes() != null && query.getLongitudes() != null
                && query.getLatitudes().size() == query.getLongitudes().size()) {
            for (int i = 0; i < query.getLatitudes().size(); i++) {
                points.add(Pair.of(query.getLongitudes().get(i), query.getLatitudes().get(i)));
            }
        }

        if (points.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        List<osmdata> geometries = osmService.getDataFromRegion(points);
        trafficEstimate = trafficService.processGeometry(geometries, query.getPeriod().get(0), query.getPeriod().get(1));        

        return ResponseEntity.status(HttpStatus.OK).body(trafficEstimate);
    }    
    
//
//    @SuppressWarnings("unchecked")
//    @ApiOperation(value = "Make a traffic query for the specified route")
//    @RequestMapping(method = RequestMethod.POST, value = "/traffic/route")
//    public ResponseEntity<TrafficEstimate> getRouteTraffic(@RequestBody RouteTrafficQuery query) throws ParseException {
//
//        TrafficEstimate trafficEstimate = null;
//
//        if (query.getLatitudes() != null) {
//            StringBuffer queryTraffic = new StringBuffer(
//                    "SELECT  * from osmdata  o where ST_Intersects(o.geometry,(ST_Buffer(CAST(ST_SetSRID(ST_MakeLine(array[");
//            for (int i = 1; i <= query.getLatitudes().size(); i++) {
//                queryTraffic.append("ST_MakePoint(" + query.getLongitudes().get(i - 1) + ","
//
//                        + query.getLatitudes().get(i - 1) + ")");
//                if (i != query.getLatitudes().size())
//                    queryTraffic.append(",");
//            }
//
//            queryTraffic.append("]");
//            queryTraffic.append(")");
//            StringBuffer secondaryQuery = new StringBuffer(queryTraffic);
//            queryTraffic.append(",4326)AS geography),10)))"); // create circles for 10m radius
//            Query queryFinal = trafficEntityManager.createNativeQuery(queryTraffic.toString(), osmdata.class);
//            List<osmdata> osmGeomtery = queryFinal.getResultList();
//            if (osmGeomtery.size() == 0) {
//                secondaryQuery.append(",4326)AS geography),200)))"); // experimental value for creating circles of 200m
//                                                                     // in case smaller radius not found for a
//                                                                     // geometry match
//                Query querySecondary = trafficEntityManager.createNativeQuery(secondaryQuery.toString(), osmdata.class);
//                osmGeomtery = querySecondary.getResultList();
//            }
//
//            trafficEstimate = processGeometry(query.getPeriod(), osmGeomtery);
//
//        }
//
//        return ResponseEntity.status(HttpStatus.OK).body(trafficEstimate);
//
//    }

//    @ApiOperation(value = "Make a traffic query for the specified region")
//    @RequestMapping(method = RequestMethod.POST, value = "/traffic/region")
//    public ResponseEntity<TrafficEstimate> getRegionTraffic(@RequestBody RegionTrafficQuery query)
//            throws ParseException {
//
//        TrafficEstimate trafficEstimate = new TrafficEstimate();
//        Boolean foundWithin = false;
//
//        if (query.getLatitudes() != null) {
//
//            StringBuffer isClosed = new StringBuffer("SELECT ST_IsClosed(ST_MakeLine(array[");
//            for (int i = 1; i <= query.getLatitudes().size(); i++) {
//                isClosed.append("ST_MakePoint(" + query.getLongitudes().get(i - 1) + ","
//
//                        + query.getLatitudes().get(i - 1) + ")");
//                if (i != query.getLatitudes().size())
//                    isClosed.append(",");
//            }
//
//            isClosed.append("]");
//            isClosed.append("))");
//            // polygon must be closed for polygon query to work
//            if ((trafficEntityManager.createNativeQuery(isClosed.toString()).getSingleResult().equals(true))) {
//                List<osmdata> osmGeomtery = returnIntersectOrWithinGeometriesMatch(query, "Within");
//                if (!osmGeomtery.isEmpty()) {
//                    foundWithin = true;
//                }
//                if (!foundWithin) {
//                    osmGeomtery = returnIntersectOrWithinGeometriesMatch(query, "Intersects");
//                }
//
//                trafficEstimate = processGeometry(query.getPeriod(), osmGeomtery);
//
//            }
//
//        }
//        return ResponseEntity.status(HttpStatus.OK).body(trafficEstimate);
//    }

//    private TrafficEstimate processGeometry(List<String> period, List<osmdata> osmGeomtery) throws ParseException {
//        TrafficEstimate trafficEstimate = new TrafficEstimate();
//
//        List<Double> impactList = new ArrayList<Double>();
//        List<Double> confidenceList = new ArrayList<Double>();
//
//        Timestamp now = DateFormatUtility.getCurrentTime();
//        String startString = period.get(0);
//        String endString = period.get(1);
//        Timestamp start = DateFormatUtility.convertStringtoTimeStamp(startString.replace("T", " "));
//        Long diff = Instant.parse(endString).getEpochSecond() - Instant.parse(startString).getEpochSecond();
//
//        // for future dates look in the past
//        if (now.before((start))) {
//            startString = Instant.parse(startString).minusSeconds(secondstoMinus).toString();
//            endString = Instant.parse(endString).minusSeconds(secondstoMinus).toString();
//        }
//
//        // do the logic for retrieving tmc id from the tmcid table calculate delta of
//        // freeflow and nominal and return delta in case past timezone
//        for (osmdata osmGeo : osmGeomtery) {
//
//            // Repository jpa style access of database --starts
//
//            List<SelectedFlowData> flowData = returnSelectedFlowdata(osmGeo.getTmc_id(), startString, endString);
//
//            if (flowData.isEmpty()) {
//                // separate condition to find data window within "known data"
//                String newStartString = Instant.now().minusSeconds(secondstoMinus).toString();
//                String newEndString = Instant.now().minusSeconds(secondstoMinus + diff).toString();
//                flowData = returnSelectedFlowdata(osmGeo.getTmc_id(), newStartString, newEndString);
//            }
//            processFlowData(impactList, confidenceList, flowData);
//
//            // Repository jpa style access of database --ends
//
//        }
//        // --currently calculating max of final impact and avg confidence scores
//        Double finalImpact = impactList.stream().mapToDouble(i -> i).max().orElse(0);
//        Double finalConfidence = confidenceList.stream().mapToDouble(i -> i).sum() / confidenceList.size();
//
//        trafficEstimate.setEstimatedDelay(finalImpact);
//        trafficEstimate.setConfidenceFactor(finalConfidence);
//
//        return trafficEstimate;
//    }
//
//    private void processFlowData(List<Double> impactList, List<Double> confidenceList,
//            List<SelectedFlowData> flowData) {
//        Double freeFlowSpeed;
//        Double nominalSpeed;
//        Double confidence;
//        Double impact;
//
//        for (SelectedFlowData flowD : flowData) {
//            freeFlowSpeed = Double.valueOf(flowD.getFree_flow_speed());
//            nominalSpeed = Double.valueOf(flowD.getSpeed());
//            confidence = Double.valueOf(flowD.getConfidence_factor());
//            impact = freeFlowSpeed < nominalSpeed ? 0 : ((freeFlowSpeed - nominalSpeed) / (freeFlowSpeed));
//            impactList.add(impact);
//            confidenceList.add(confidence);
//        }
//    }
//
//    public List<SelectedFlowData> returnSelectedFlowdata(String tmc_id, String timeSlot1, String timeSlot2) {
////	     List<SelectedFlowData> flowData = flowDataRepository.findFlowDataInformationbytmcIdandtimeslot(tmc_id,
////      timeSlot1, timeSlot2);
//
//        String query = "SELECT * from selectedflowdata s where s.tmc_id = ? AND s.timestamp BETWEEN CAST(? AS timestamp) AND CAST(? AS timestamp) ORDER BY jam_factor DESC LIMIT 3";
//        Query queryFinal = trafficEntityManager.createNativeQuery(query, osmdata.class);
//        queryFinal.setParameter(1, Long.parseLong(tmc_id));
//        queryFinal.setParameter(2, timeSlot1);
//        queryFinal.setParameter(3, timeSlot2);
//
//        List<SelectedFlowData> flowData = queryFinal.getResultList();
//
//        return flowData;
//    }
//
//    @SuppressWarnings("unchecked")
//    public List<osmdata> returnIntersectOrWithinGeometriesMatch(RegionTrafficQuery query, String parameter) {
//
//        StringBuffer queryTraffic = new StringBuffer("SELECT  * from osmdata  o where" + " " + "ST_" + parameter
//                + "(o.geometry,(ST_SetSRID(ST_MakePolygon(ST_MakeLine(array[");
//        for (int i = 1; i <= query.getLatitudes().size(); i++) {
//            queryTraffic.append("ST_MakePoint(" + query.getLongitudes().get(i - 1) + ","
//
//                    + query.getLatitudes().get(i - 1) + ")");
//            if (i != query.getLatitudes().size())
//                queryTraffic.append(",");
//        }
//
//        queryTraffic.append("]");
//        queryTraffic.append("))");
//        queryTraffic.append(",4326)))");
//        Query queryFinal = trafficEntityManager.createNativeQuery(queryTraffic.toString(), osmdata.class);
//        List<osmdata> osmGeomtery = queryFinal.getResultList();
//        return osmGeomtery;
//
//    }

}
