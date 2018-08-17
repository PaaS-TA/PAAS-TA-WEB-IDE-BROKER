package org.paasta.servicebroker.webide.model;

import org.openpaas.servicebroker.model.ServiceInstance;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;



/**
 * Created by Mingu on 2017-05-10.
 */
@Entity
@Table(name = "service_instance")
public class JpaServiceInstance {
    @Id
    @Column(name = "service_instance_id")
    private String serviceInstanceId;
    @Column(name = "service_id")
    private String serviceDefinitionId;
    @Column(name = "plan_id")
    private String planId;
    @Column(name = "organization_guid")
    private String organizationGuid;
    @Column(name = "space_guid")
    private String spaceGuid;
    @Column(name = "dashboard_url")
    private String dashboardUrl;

    public JpaServiceInstance() { }

    public JpaServiceInstance(ServiceInstance serviceInstance) {
        this.serviceInstanceId = serviceInstance.getServiceInstanceId();
        this.serviceDefinitionId = serviceInstance.getServiceDefinitionId();
        this.planId = serviceInstance.getPlanId();
        this.organizationGuid = serviceInstance.getOrganizationGuid();
        this.spaceGuid = serviceInstance.getSpaceGuid();
        this.dashboardUrl = serviceInstance.getDashboardUrl();

    }

    public String getServiceInstanceId() {
        return serviceInstanceId;
    }

    public void setServiceInstanceId(String serviceInstanceId) {
        this.serviceInstanceId = serviceInstanceId;
    }

    public String getServiceDefinitionId() {
        return serviceDefinitionId;
    }

    public void setServiceDefinitionId(String serviceDefinitionId) {
        this.serviceDefinitionId = serviceDefinitionId;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getOrganizationGuid() {
        return organizationGuid;
    }

    public void setOrganizationGuid(String organizationGuid) {
        this.organizationGuid = organizationGuid;
    }

    public String getSpaceGuid() {
        return spaceGuid;
    }

    public void setSpaceGuid(String spaceGuid) {
        this.spaceGuid = spaceGuid;
    }

    public String getDashboardUrl() {
        return dashboardUrl;
    }

    public void setDashboardUrl(String dashboardUrl) {
        this.dashboardUrl = dashboardUrl;
    }
}