package org.paasta.servicebroker.webide.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.openpaas.servicebroker.model.CreateServiceInstanceRequest;
import org.openpaas.servicebroker.model.DeleteServiceInstanceRequest;
import org.openpaas.servicebroker.model.ServiceInstance;
import org.openpaas.servicebroker.model.UpdateServiceInstanceRequest;
import org.springframework.data.annotation.Transient;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.HashMap;
import java.util.Map;

/**
 * web_ide_broker 에 대한 Entity 모델 클래스 (JPA를 이용해서 사용)
 *
 * @author sjchoi
 * @since 2018.08.14
 * @version 1.0
 */

@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE)
@Entity
@Table(name = "web_ide_broker")
public class JpaServiceInstance extends ServiceInstance {

    @JsonSerialize
    @JsonProperty("user_id")
    @Column(name = "user_id")
    private String userId;

    @JsonSerialize
    @JsonProperty("dashboard_url")
    @Column(name = "dashboard_url")
    private String dashboardUrl;

    @JsonSerialize
    @JsonProperty("organization_guid")
    @Column(name = "organization_guid")
    private String organizationGuid;

    @JsonSerialize
    @JsonProperty("plan_id")
    @Column(name = "plan_id")
    private String planId;

    @JsonSerialize
    @JsonProperty("service_id")
    @Column(name = "service_id")
    private String serviceId;

    @JsonSerialize
    @JsonProperty("space_guid")
    @Column(name = "space_guid")
    private String spaceGuid;

    /**
     * CreateServiceInstanceRequest가 들어왔을 경우의 생성자 (서비스 인스턴스 생성)
     *
     * @param request
     */
    public JpaServiceInstance(CreateServiceInstanceRequest request) {
        super(request);
    }

    /**
     * DeleteServiceInstanceRequest가 들어왔을 경우의 생성자 (서비스 인스턴스 삭제)
     *
     * @param request
     */
    public JpaServiceInstance(DeleteServiceInstanceRequest request) {
        super(request);
    }

    /**
     * UpdateServiceInstanceRequest가 들어왔을 경우의 생성자 (서비스 인스턴스 수정)
     *
     * @param request
     */
    public JpaServiceInstance(UpdateServiceInstanceRequest request) {
        super(request);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Id
    @Column(name = "dashboard_url")
    @Override
    public String getDashboardUrl() {
        return dashboardUrl;
    }

    public void setDashboardUrl(String dashboardUrl) {
        this.dashboardUrl = dashboardUrl;
    }

    @Override
    public String getOrganizationGuid() {
        return organizationGuid;
    }

    public void setOrganizationGuid(String organizationGuid) {
        this.organizationGuid = organizationGuid;
    }

    @Override
    public String getPlanId() {
        return planId;
    }

    @Override
    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    @Override
    public String getSpaceGuid() {
        return spaceGuid;
    }

    public void setSpaceGuid(String spaceGuid) {
        this.spaceGuid = spaceGuid;
    }


}
