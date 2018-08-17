package org.paasta.servicebroker.webide.model;

import org.openpaas.servicebroker.model.ServiceInstance;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;


/**
 * web_ide_broker 에 대한 Entity 모델 클래스
 *
 * @author sjchoi
 * @since 2018.08.14
 * @version 1.0
 */

@Entity
@Table(name = "web_ide_broker")
public class WebIde {

    @Column(name = "user_id")
    private String userId;
    @Column(name = "organization_guid")
    private String organizationGuid;
    @Id
    @Column(name = "dashboard_url")
    private String dashboardUrl;
    @Column(name = "create_yn")
    private String createYn;
    @Column(name = "created_at")
    private Date createdAt;
    @Column(name = "updated_at")
    private Date updatedAt;
    @Column(name = "service_instance_id")
    private String serviceInstanceId;

    public WebIde() { }

    public WebIde(ServiceInstance serviceInstance) {
        this.organizationGuid = serviceInstance.getOrganizationGuid();
        this.dashboardUrl = serviceInstance.getDashboardUrl();
        this.serviceInstanceId = serviceInstance.getServiceInstanceId();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrganizationGuid() {
        return organizationGuid;
    }

    public void setOrganizationGuid(String organizationGuid) {
        this.organizationGuid = organizationGuid;
    }

    public String getDashboardUrl() {
        return dashboardUrl;
    }

    public void setDashboardUrl(String dashboardUrl) {
        this.dashboardUrl = dashboardUrl;
    }

    public String getCreateYn() {
        return createYn;
    }

    public void setCreateYn(String createYn) {
        this.createYn = createYn;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getServiceInstanceId() {
        return serviceInstanceId;
    }

    public void setServiceInstanceId(String serviceInstanceId) {
        this.serviceInstanceId = serviceInstanceId;
    }
}