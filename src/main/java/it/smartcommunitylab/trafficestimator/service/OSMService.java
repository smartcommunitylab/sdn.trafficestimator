package it.smartcommunitylab.trafficestimator.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import it.smartcommunitylab.trafficestimator.model.osmdata;

@Component
public class OSMService {

    @Autowired
    @Qualifier("trafficEntityManager")
    private EntityManager osmEntityManager;

    public List<osmdata> getDataFromRoute(List<Pair<Double, Double>> points, int buffer) throws Exception {

        // build query with buffer in meters
        StringBuffer queryTraffic = new StringBuffer(
                "SELECT * from osmdata o where ST_Intersects(o.geometry,(ST_Buffer(CAST(ST_SetSRID(ST_MakeLine(array[");

        for (int i = 1; i <= points.size(); i++) {
            queryTraffic.append("ST_MakePoint(");
            queryTraffic.append(points.get(i - 1).getFirst() + "," + points.get(i - 1).getSecond());
            queryTraffic.append(")");

            if (i != points.size()) {
                queryTraffic.append(",");
            }
        }

        queryTraffic.append("])");
        queryTraffic.append(",4326)AS geography)," + Integer.toString(buffer) + ")))"); // create circles for 10m radius

        Query queryFinal = osmEntityManager.createNativeQuery(queryTraffic.toString(), osmdata.class);
        List<osmdata> results = queryFinal.getResultList();

        return results;
    }

    public List<osmdata> getDataFromRegion(List<Pair<Double, Double>> points) throws Exception {

        // check if polygon is closed
        StringBuffer queryClosed = new StringBuffer("SELECT ST_IsClosed(ST_MakeLine(array[");
        for (int i = 1; i <= points.size(); i++) {
            queryClosed.append("ST_MakePoint(");
            queryClosed.append(points.get(i - 1).getFirst() + "," + points.get(i - 1).getSecond());
            queryClosed.append(")");

            if (i != points.size()) {
                queryClosed.append(",");
            }
        }

        queryClosed.append("]");
        queryClosed.append("))");

        boolean isClosed = osmEntityManager.createNativeQuery(queryClosed.toString()).getSingleResult().equals(true);

        if (!isClosed) {
            return null;
        }

        List<osmdata> results = returnWithinGeometriesMatch(points);
        if (results.isEmpty()) {
            results = returnIntersectGeometriesMatch(points);
        }

        return results;

    }

    public List<osmdata> returnIntersectGeometriesMatch(List<Pair<Double, Double>> points) throws Exception {

        StringBuffer queryTraffic = new StringBuffer(
                "SELECT * from osmdata o where ST_Intersects(o.geometry,(ST_SetSRID(ST_MakePolygon(ST_MakeLine(array[");

        for (int i = 1; i <= points.size(); i++) {
            queryTraffic.append("ST_MakePoint(");
            queryTraffic.append(points.get(i - 1).getFirst() + "," + points.get(i - 1).getSecond());
            queryTraffic.append(")");

            if (i != points.size()) {
                queryTraffic.append(",");
            }
        }

        queryTraffic.append("]");
        queryTraffic.append("))");
        queryTraffic.append(",4326)))");

        Query queryFinal = osmEntityManager.createNativeQuery(queryTraffic.toString(), osmdata.class);
        List<osmdata> results = queryFinal.getResultList();

        return results;

    }

    public List<osmdata> returnWithinGeometriesMatch(List<Pair<Double, Double>> points) throws Exception {

        StringBuffer queryTraffic = new StringBuffer(
                "SELECT * from osmdata o where ST_Within(o.geometry,(ST_SetSRID(ST_MakePolygon(ST_MakeLine(array[");

        for (int i = 1; i <= points.size(); i++) {
            queryTraffic.append("ST_MakePoint(");
            queryTraffic.append(points.get(i - 1).getFirst() + "," + points.get(i - 1).getSecond());
            queryTraffic.append(")");

            if (i != points.size()) {
                queryTraffic.append(",");
            }
        }

        queryTraffic.append("]");
        queryTraffic.append("))");
        queryTraffic.append(",4326)))");

        Query queryFinal = osmEntityManager.createNativeQuery(queryTraffic.toString(), osmdata.class);
        List<osmdata> results = queryFinal.getResultList();

        return results;

    }
}
