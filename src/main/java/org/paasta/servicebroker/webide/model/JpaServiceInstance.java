package org.paasta.servicebroker.webide.model;

import org.openpaas.servicebroker.model.ServiceInstance;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "web_ide_info")
public class JpaServiceInstance {

    @Id
    @Column(name = "dashboard_url")
    private String dashboardUrl;

    @Column(name = "use_yn")
    private String useYn;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "plan_id")
    private String planId;

    @Column(name = "service_id")
    private String serviceDefinitionId;

    @Column(name = "service_instance_id")
    private String serviceInstanceId;

    @Column(name = "space_guid")
    private String spaceGuid;

    @Column(name = "organization_guid")
    private String organizationGuid;

    public JpaServiceInstance() {
    }

    public JpaServiceInstance(ServiceInstance serviceInstance) {
        this.dashboardUrl = serviceInstance.getDashboardUrl();
        this.planId = serviceInstance.getPlanId();
        this.serviceDefinitionId = serviceInstance.getServiceDefinitionId();
        this.serviceInstanceId = serviceInstance.getServiceInstanceId();
        this.spaceGuid = serviceInstance.getSpaceGuid();
        this.organizationGuid = serviceInstance.getOrganizationGuid();
    }


    public String getDashboardUrl() {
        return dashboardUrl;
    }

    public void setDashboardUrl(String dashboardUrl) {
        this.dashboardUrl = dashboardUrl;
    }

    public String getUseYn() {
        return useYn;
    }

    public void setUseYn(String useYn) {
        this.useYn = useYn;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getServiceDefinitionId() {
        return serviceDefinitionId;
    }

    public void setServiceDefinitionId(String serviceDefinitionId) {
        this.serviceDefinitionId = serviceDefinitionId;
    }

    public String getServiceInstanceId() {
        return serviceInstanceId;
    }

    public void setServiceInstanceId(String serviceInstanceId) {
        this.serviceInstanceId = serviceInstanceId;
    }

    public String getSpaceGuid() {
        return spaceGuid;
    }

    public void setSpaceGuid(String spaceGuid) {
        this.spaceGuid = spaceGuid;
    }

    public String getOrganizationGuid() {
        return organizationGuid;
    }

    public void setOrganizationGuid(String organizationGuid) {
        this.organizationGuid = organizationGuid;
    }
}