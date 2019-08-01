package it.smartcommunitylab.trafficestimator.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import it.smartcommunitylab.trafficestimator.model.osmdata;


@Repository
public interface OSMRepository extends JpaRepository<osmdata,Object>,JpaSpecificationExecutor<osmdata> {
	
	 
	 
	 @Query(value="SELECT ST_Distance(o.geometry,ST_GeomFromText(?#{[0]},?#{[1]})) from osmdata o;", nativeQuery = true)
		public List<osmdata> findDistancebyGeometry(String text1,Integer number);
	 
	 @Query(value="SELECT  * from osmdata where ST_Touches(l.geom,ST_GeomFromText(?#{[0]},?#{[1]}))", nativeQuery = true)
		public List<String> findTouchesbyGeometry(String text1,Integer number);
	 
	 @Query(value="SELECT  * from osmdata  o where ST_Intersects(o.geometry,ST_Buffer(CAST(ST_SetSRID(ST_Point(?#{[0]},?#{[1]}),4326) AS geography),102))",nativeQuery=true)
	 public List<osmdata> findIntersectionGeometry(Double d1, Double d2);
	 
	 
	 


	 
}
