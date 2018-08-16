package org.paasta.servicebroker.webide.repo;

import org.paasta.servicebroker.webide.model.JpaServiceInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaServiceInstanceRepository extends JpaRepository<JpaServiceInstance, String>{
    JpaServiceInstance findByOrganizationGuid(String organizationId);
}
