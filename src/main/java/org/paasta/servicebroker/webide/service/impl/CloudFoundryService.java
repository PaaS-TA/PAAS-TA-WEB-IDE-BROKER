package org.paasta.servicebroker.webide.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.cloudfoundry.client.v2.applications.ApplicationsV2;
import org.cloudfoundry.client.v2.applications.RestageApplicationRequest;
import org.cloudfoundry.client.v2.securitygroups.CreateSecurityGroupRequest;
import org.cloudfoundry.client.v2.securitygroups.DeleteSecurityGroupRequest;
import org.cloudfoundry.client.v2.securitygroups.GetSecurityGroupRequest;
import org.cloudfoundry.client.v2.securitygroups.ListSecurityGroupsRequest;
import org.cloudfoundry.client.v2.securitygroups.ListSecurityGroupsResponse;
import org.cloudfoundry.client.v2.securitygroups.Protocol;
import org.cloudfoundry.client.v2.securitygroups.RuleEntity;
import org.cloudfoundry.client.v2.securitygroups.SecurityGroupResource;
import org.cloudfoundry.client.v2.securitygroups.SecurityGroups;
import org.cloudfoundry.client.v2.securitygroups.UpdateSecurityGroupRequest;
import org.cloudfoundry.client.v2.servicebindings.CreateServiceBindingRequest;
import org.cloudfoundry.client.v2.servicebindings.ServiceBindingsV2;
import org.cloudfoundry.client.v2.serviceinstances.ListServiceInstancesRequest;
import org.cloudfoundry.client.v2.serviceinstances.ListServiceInstancesResponse;
import org.cloudfoundry.client.v2.serviceinstances.ServiceInstances;
import org.cloudfoundry.client.v2.serviceplans.ListServicePlansRequest;
import org.cloudfoundry.client.v2.serviceplans.ListServicePlansResponse;
import org.cloudfoundry.client.v2.serviceplans.ServicePlans;
import org.cloudfoundry.client.v2.services.ListServicesRequest;
import org.cloudfoundry.client.v2.services.ListServicesResponse;
import org.cloudfoundry.client.v2.services.Services;
import org.cloudfoundry.client.v2.spaces.GetSpaceRequest;
import org.cloudfoundry.client.v2.spaces.GetSpaceResponse;
import org.cloudfoundry.client.v2.spaces.Spaces;
import org.paasta.servicebroker.webide.common.Common;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CloudFoundryService {

    @Value("${bosh.instance_name}")
    public String instance_name;

    @Autowired
    Common common;

    private static final Logger logger = LoggerFactory.getLogger(CloudFoundryService.class);

    public void ServiceInstanceAppBinding(String AppId, String ServiceInstanceId, Map parameters, ServiceBindingsV2 serviceBindingV2, ApplicationsV2 applicationsV2) throws Exception{
        serviceBindingV2.create(CreateServiceBindingRequest.builder().applicationId(AppId)
                        .serviceInstanceId(ServiceInstanceId).parameters(parameters).build()).block();
        applicationsV2.restage(RestageApplicationRequest.builder().applicationId(AppId).build()).block();
    }

    public void SecurityGurop(String space_id, String url, SecurityGroups securityGroups) throws Exception{
        /*
        시큐리티 그룹 조회
         */
        ListSecurityGroupsResponse  listSecurityGroupsResponse = securityGroups.list(ListSecurityGroupsRequest.builder().build()).block();
        List<SecurityGroupResource> resources = listSecurityGroupsResponse.getResources().stream().filter(result -> result.getEntity().getName().equals(instance_name + "_" + space_id)).collect(Collectors.toList());
        if(resources.isEmpty()){
            for(int i = 2 ; i <= listSecurityGroupsResponse.getTotalPages(); i++)
            {
                resources = securityGroups.list(ListSecurityGroupsRequest.builder().page(i).build()).block().getResources().stream().filter(result -> result.getEntity().getName().equals(instance_name + "_" + space_id)).collect(Collectors.toList());
                if(!resources.isEmpty()){
                    UpdateSecurityGroup(securityGroups, url, resources);
                   return;
                }
            }
        } else {
            UpdateSecurityGroup(securityGroups, url, resources);
            return;
        }
	try {
        securityGroups.create(CreateSecurityGroupRequest.builder()
                .name(instance_name + "_" + space_id)
                .rule(RuleEntity.builder()
                        .protocol(Protocol.ALL)
                        .destination(url)
                        .build())
                .spaceId(space_id)
                .build()).block();
	}catch(Exception e){
		logger.info(e.getMessage());
	}
    }

    public void UpdateSecurityGroup(SecurityGroups securityGroups, String url, List<SecurityGroupResource> resources){
        try {
            List<RuleEntity> ruleEntities = securityGroups.get(GetSecurityGroupRequest.builder().securityGroupId(resources.get(0).getMetadata().getId()).build()).block().getEntity().getRules();
            List<RuleEntity> rules = new ArrayList<>();
            rules.add(RuleEntity.builder()
                    .protocol(Protocol.ALL)
                    .destination(url)
                    .build());
            rules.addAll(ruleEntities);
            securityGroups.update(UpdateSecurityGroupRequest.builder().name(resources.get(0).getEntity().getName()).securityGroupId(resources.get(0).getMetadata().getId()).rules(rules).build()).block();
        } catch (Exception e){
            logger.info(e.getMessage());
        }
    }

    public void DelSecurityGurop(SecurityGroups securityGroups, String space_id, String url){
        /*
        시큐리티 그룹 조회
         */
        ListSecurityGroupsResponse  listSecurityGroupsResponse = securityGroups.list(ListSecurityGroupsRequest.builder().build()).block();
        List<SecurityGroupResource> resources = listSecurityGroupsResponse.getResources().stream().filter(result -> result.getEntity().getName().equals(instance_name + "_" + space_id)).collect(Collectors.toList());
        if(resources.isEmpty()){
            for(int i = 2 ; i <= listSecurityGroupsResponse.getTotalPages(); i++)
            {
                resources = securityGroups.list(ListSecurityGroupsRequest.builder().page(i).build()).block().getResources().stream().filter(result -> result.getEntity().getName().equals(instance_name + "_" + space_id)).collect(Collectors.toList());
                if(!resources.isEmpty()){
                    DelUpdateSecurityGroup(securityGroups, url, resources);
                    return;
                }
            }
        } else {
            DelUpdateSecurityGroup(securityGroups, url, resources);
            return;
        }
    }

    private void DelUpdateSecurityGroup(SecurityGroups securityGroups, String url, List<SecurityGroupResource> resources){
        try {
            List<RuleEntity> ruleEntities = securityGroups.get(GetSecurityGroupRequest.builder().securityGroupId(resources.get(0).getMetadata().getId()).build()).block().getEntity().getRules();
            List<RuleEntity> rules = new ArrayList<>();
            rules.addAll(ruleEntities);
            if(rules.size() <= 1){
                securityGroups.delete(DeleteSecurityGroupRequest.builder().securityGroupId(resources.get(0).getMetadata().getId()).async(true).build());
            }else{
                for(RuleEntity rule : rules) {
                    if (rule.getDestination().equals(url)) {
                        rules.remove(rule);
                        break;
                    }
                }
                securityGroups.update(UpdateSecurityGroupRequest.builder().name(resources.get(0).getEntity().getName()).securityGroupId(resources.get(0).getMetadata().getId()).rules(rules).build()).block();
            }
        } catch (Exception e){
            logger.info(e.getMessage());
        }
    }
    
    public ListServiceInstancesResponse getServiceList() {
    	ServiceInstances serviceInstances = common.cloudFoundryClient().serviceInstances();
    	return serviceInstances.list(ListServiceInstancesRequest.builder().build()).block();
    }
    
    public GetSpaceResponse getSpace(String spaceId) {
    	Spaces spaces = common.cloudFoundryClient().spaces();
    	return spaces.get(GetSpaceRequest.builder().spaceId(spaceId).build()).block();
    }
    
    public ListServicesResponse getServices() {
    	Services services = common.cloudFoundryClient().services();
    	return services.list(ListServicesRequest.builder().build()).block();
    }
    
    public ListServicePlansResponse getServicePlans() {
    	ServicePlans servicePlans = common.cloudFoundryClient().servicePlans();
    	return servicePlans.list(ListServicePlansRequest.builder().build()).block();
    }
}
