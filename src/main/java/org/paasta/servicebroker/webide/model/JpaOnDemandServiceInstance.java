package org.paasta.servicebroker.webide.model;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.openpaas.servicebroker.model.CreateServiceInstanceRequest;
import org.openpaas.servicebroker.model.ServiceInstance;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@Table(name = "on_demand_info")
public class JpaOnDemandServiceInstance extends ServiceInstance {
	@JsonSerialize
    @JsonProperty("service_instance_id")
    @Id
    @Column(name = "service_instance_id")
    private String serviceInstanceId;

    @JsonSerialize
    @Column(name = "service_id")
    @JsonProperty("service_id")
    private String serviceDefinitionId;

    @JsonSerialize
    @Column(name = "plan_id")
    @JsonProperty("plan_id")
    private String planId;

    @JsonSerialize
    @Column(name = "organization_guid")
    @JsonProperty("organization_guid")
    private String organizationGuid;

    @JsonSerialize
    @Column(name = "space_guid")
    @JsonProperty("space_guid")
    private String spaceGuid;

    @JsonSerialize
    @Column(name = "dashboard_url")
    @JsonProperty("dashboard_url")
    private String dashboardUrl;

    @Transient
    @JsonIgnore
    private boolean async;

    @JsonSerialize
    @JsonProperty("vm_instance_id")
    @Column(name = "vm_instance_id")
    private String vmInstanceId;

    @JsonSerialize
    @JsonProperty("app_guid")
    @Column(name = "app_guid")
    private String appGuid;

    @JsonSerialize
    @JsonProperty("task_id")
    @Column(name = "task_id")
    private String taskId;

    @JsonSerialize
    @JsonProperty("app_parameter")
    @Column(name = "app_parameter")
    private String app_parameter;
    
    public JpaOnDemandServiceInstance() {
        super();
    }
    
    public JpaOnDemandServiceInstance(CreateServiceInstanceRequest request) {
        super(request);
        setServiceDefinitionId(request.getServiceDefinitionId());
        setPlanId(request.getPlanId());
        setOrganizationGuid(request.getOrganizationGuid());
        setSpaceGuid(request.getSpaceGuid());
        setServiceInstanceId(request.getServiceInstanceId());
        AtomicReference<String> param = new AtomicReference<>("{");
        AtomicInteger i = new AtomicInteger(1);
        try {
            if (request.getParameters() != null) {
                request.getParameters().forEach((key, value) -> {
                    if (key.equals("app_guid")) {
                        setAppGuid(value.toString());
                    }
                    param.set(param.get() + "\"" + key + "\":\"" + value.toString() + "\"");
                    if (i.get() < request.getParameters().size()) {
                        param.set(param.get() + ",");
                    }
                    i.set(i.get() + 1);
                });
            }
        }catch (Exception e){
        }
        param.set(param.get() + "}");
        setApp_parameter(param.get());
    }
    
    @Override
    public String getDashboardUrl() {
        return dashboardUrl;
    }

    public void setDashboardUrl(String dashboardUrl) {
        this.dashboardUrl = dashboardUrl;
    }

    public String getAppGuid() {
        return appGuid;
    }

    public void setAppGuid(String appGuid) {
        this.appGuid = appGuid;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @Override
    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    @Override
    public String getServiceDefinitionId() {
        return serviceDefinitionId;
    }

    public void setServiceDefinitionId(String serviceDefinitionId) {
        this.serviceDefinitionId = serviceDefinitionId;
    }

    @Override
    public String getServiceInstanceId() {
        return serviceInstanceId;
    }

    public void setServiceInstanceId(String serviceInstanceId) {
        this.serviceInstanceId = serviceInstanceId;
    }

    @Override
    public String getSpaceGuid() {
        return spaceGuid;
    }

    public void setSpaceGuid(String spaceGuid) {
        this.spaceGuid = spaceGuid;
    }

    @Override
    public String getOrganizationGuid() {
        return organizationGuid;
    }

    public void setOrganizationGuid(String organizationGuid) {
        this.organizationGuid = organizationGuid;
    }

    public String getVmInstanceId() {
        return vmInstanceId;
    }

    public void setVmInstanceId(String vmInstanceId) {
        this.vmInstanceId = vmInstanceId;
    }

    @Override
    public boolean isAsync() {
        return async;
    }

    @Override
    public ServiceInstance and() {
        return this;
    }

    @Override
    public JpaOnDemandServiceInstance withDashboardUrl(String dashboardUrl) {
        this.dashboardUrl = dashboardUrl;
        return this;
    }

    @Override
    public ServiceInstance withAsync(boolean async) {
        this.async = async;
        return this;
    }


    public String getApp_parameter() {
        return app_parameter;
    }

    public void setApp_parameter(String app_parameter) {
        this.app_parameter = app_parameter;
    }

    public void setAsync(boolean async) {
        this.async = async;
    }

    @Override
    public String toString() {
        return "JpaServiceInstance{" +
                "serviceInstanceId='" + serviceInstanceId + '\'' +
                ", serviceDefinitionId='" + serviceDefinitionId + '\'' +
                ", planId='" + planId + '\'' +
                ", organizationGuid='" + organizationGuid + '\'' +
                ", spaceGuid='" + spaceGuid + '\'' +
                ", dashboardUrl='" + dashboardUrl + '\'' +
                ", async=" + async +
                ", vmInstanceId='" + vmInstanceId + '\'' +
                ", appGuid='" + appGuid + '\'' +
                ", taskId='" + taskId + '\'' +
                '}';
    }

}
