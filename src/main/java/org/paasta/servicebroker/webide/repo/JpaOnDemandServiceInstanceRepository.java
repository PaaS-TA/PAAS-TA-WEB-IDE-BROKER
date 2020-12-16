package org.paasta.servicebroker.webide.repo;

import org.paasta.servicebroker.webide.model.JpaOnDemandServiceInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Web-Ide 서비스 관련 에러 Exception 클래스
 *
 * @author sjchoi
 * @since 2018.08.14
 */

@Repository
public interface JpaOnDemandServiceInstanceRepository extends JpaRepository<JpaOnDemandServiceInstance, String>{
	List<JpaOnDemandServiceInstance> findAllByOrganizationGuid(String organizationId);

    List<JpaOnDemandServiceInstance> findAllBySpaceGuid(String spaceId);

    JpaOnDemandServiceInstance findByVmInstanceIdAndOrganizationGuid(String vmInstanceId, String organizationId);

    boolean existsByOrganizationGuid(String organizationId);

    boolean existsByServiceInstanceId(String instanceID);

    JpaOnDemandServiceInstance findByServiceInstanceId(String serviceInstanceId);

    JpaOnDemandServiceInstance findByDashboardUrl(String dashboardurl);

    JpaOnDemandServiceInstance findByVmInstanceId(String vmInstanceId);

    boolean existsAllByVmInstanceId(String vmInstanceId);

    boolean existsAllByDashboardUrl(String dashboardUrl);

    JpaOnDemandServiceInstance findByServiceInstanceIdAndDashboardUrl(String serviceInstanceId, String dashboardUrl);

    void deleteByServiceInstanceId(String id);


}
