package it.smartcommunitylab.trafficestimator.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import it.smartcommunitylab.trafficestimator.model.SelectedFlowData;

@Repository
public interface FlowDataRepository extends JpaRepository<SelectedFlowData,Object>,JpaSpecificationExecutor<SelectedFlowData>  {

	/*
	 * @Query(
	 * value="SELECT free_flow_speed,speed,confidence_factor from selectedflowdata s where s.tmc_id = (?#{[0]}) AND (s.timestamp::timestamp) BETWEEN (?#{[1]}::timestamp) AND (?#{[2]}::timestamp)"
	 * ,nativeQuery=true) public List<SelectedFlowData>
	 * findFlowDataInformationbytmcIdandtimeslot(String tmc_id,String period1,String
	 * period2);
	 */


	@Query(value="SELECT * from SelectedFlowData s where s.tmc_id = CAST(?#{[0]} as INTEGER)"+ " AND s.timestamp BETWEEN CAST(?#{[1]} AS timestamp) "+" AND CAST(?#{[2]} AS timestamp) ORDER BY jam_factor DESC LIMIT 3",nativeQuery=true)
//	@Query(value="SELECT * from SelectedFlowData s where s.tmc_id = ?#{[0]}"+ " AND CAST(s.timestamp AS timestamp) BETWEEN CAST(?#{[1]} AS timestamp) "+" AND CAST(?#{[2]} AS timestamp)",nativeQuery=true)
	 public List<SelectedFlowData> findFlowDataInformationbytmcIdandtimeslot(String tmc_id,String slot1,String slot2);


}
