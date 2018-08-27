package org.paasta.servicebroker.webide.repo;

import org.paasta.servicebroker.webide.model.JpaServiceInstance;
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
public interface JpaServiceInstanceRepository extends JpaRepository<JpaServiceInstance, String>{
    JpaServiceInstance findByOrganizationGuid(String organizationId);

    JpaServiceInstance findByServiceInstanceId(String serviceInstanceId);

    List<JpaServiceInstance> findByUseYn(String use_yn);

}
