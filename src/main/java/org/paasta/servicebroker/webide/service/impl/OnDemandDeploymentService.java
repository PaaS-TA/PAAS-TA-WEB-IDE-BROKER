package org.paasta.servicebroker.webide.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.openpaas.paasta.bosh.director.BoshDirector;
import org.paasta.servicebroker.webide.model.DeploymentInstance;
import org.paasta.servicebroker.webide.model.DeploymentLock;
import org.paasta.servicebroker.webide.model.JpaOnDemandServiceInstance;
import org.paasta.servicebroker.webide.repo.JpaOnDemandServiceInstanceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.google.gson.Gson;


@Service
public class OnDemandDeploymentService {

	private static final Logger logger = LoggerFactory.getLogger(OnDemandDeploymentService.class);

	private BoshDirector boshDirector;
	
	@Autowired
	JpaOnDemandServiceInstanceRepository jpaServiceInstanceRepository;
	
	@Autowired
	public void setBoshDirector(BoshDirector boshDirector) {
		this.boshDirector = boshDirector;
	}
	
	public List<DeploymentInstance> getVmInstance(String deploymentName, String instanceName) {
		
		try {
			String tasks = boshDirector.getListDetailOfInstances(deploymentName);
			List<DeploymentInstance> deploymentInstances = new ArrayList<DeploymentInstance>();
			
			Thread.sleep(2000);
			
			List<Map> results = null;
			if (StringUtils.isEmpty(boshDirector.getBosh_version()) || "2700100".compareTo(boshDirector.getBosh_version()) >= 0) {
                results = boshDirector.getResultRetrieveTasksLog(tasks);
            } else {
                results = boshDirector.getResultRetrieveTasksLogv271(tasks);
            }
			
			for (Map map : results) {
                if (map.get("job_name").equals(instanceName)) {
                    DeploymentInstance deploymentInstance = new DeploymentInstance(map);
                    deploymentInstances.add(deploymentInstance);
                }
            }
			
            return deploymentInstances;
		} catch (Exception e) {
			return null;
		}
	}
	
	public boolean getLock(String deploymentName) {
		try {
            String locks = boshDirector.getListLocks();
                JSONArray jsonArray = new JSONArray(locks);
                for (int i = 0; i < jsonArray.length(); i++) {
                    String json = jsonArray.get(i).toString();
                    DeploymentLock dataJson = new Gson().fromJson(json, DeploymentLock.class);
                    if (dataJson.resuource[0].equals(deploymentName)) {
                        return true;
                    }
                }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
        return false;
	}
	
	public void updateInstanceState(String deploymentName, String instanceName, String instanceId, String type) {
        try {
                boshDirector.updateInstanceState(deploymentName, instanceName, instanceId, type);
        } catch (Exception e) {
        	e.printStackTrace();
            logger.error(e.getMessage());
        }
    }

    public boolean runningTask(String deploymentName, JpaOnDemandServiceInstance instance) {
        try {
            List<Map> deployTask = boshDirector.getListRunningTasks();
            List<Map> running_deployTask = deployTask.stream().filter(r -> r.get("deployment").equals(deploymentName)).collect(Collectors.toList());
            if (running_deployTask.isEmpty()) {
                return true;
            } else {
                if (instance.getTaskId() == null) {
                    instance.setTaskId(running_deployTask.get(0).get("id").toString());
                    jpaServiceInstanceRepository.save(instance);
                }
                running_deployTask = running_deployTask.stream().filter(r -> r.get("id").toString().equals(instance.getTaskId())).collect(Collectors.toList());
                if (running_deployTask.isEmpty()) {
                    return true;
                }
                return false;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    public String getTaskID(String deploymentName) {
        try {
            List<Map> deployTask = boshDirector.getListRunningTasks();
            List<Map> running_deployTask = deployTask.stream().filter(r -> r.get("deployment").equals(deploymentName)).collect(Collectors.toList());
            if (running_deployTask.isEmpty()) {
                return null;
            }
            return running_deployTask.get(0).get("id").toString();
        } catch (Exception e) {
            return null;
        }
    }

    public String getStartInstanceIPS(String taksId, String instanceName, String instanceId) {
        try {
            return boshDirector.getStartVMIPS(taksId, instanceName, instanceId);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    public void createInstance(String deploymentName, String instanceName) throws Exception {
    	this.createInstance(deploymentName, instanceName, null);
    }
    
    public void createInstance(String deploymentName, String instanceName, String staticIp) throws Exception {
    	try {
        	Map map = boshDirector.getDeployments(deploymentName);
            boshDirector.deploy(deploymentName, instanceName, staticIp);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public String getUpdateInstanceIPS(String taskId) {
        try {
            return boshDirector.getUpdateVMIPS(taskId);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    public String getUpdateVMInstanceID(String taskId, String instanceName) {
        try {
            return boshDirector.getUpdateVMInstance(taskId, instanceName);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }
}
