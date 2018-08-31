package org.paasta.servicebroker.webide.repo;

import org.paasta.servicebroker.webide.model.JpaServiceList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Web-Ide 서비스 관련 에러 Exception 클래스
 *
 * @author sjchoi
 * @since 2018.08.14
 */

@Repository
public interface JpaServiceListRepository extends JpaRepository<JpaServiceList, String>{

}
