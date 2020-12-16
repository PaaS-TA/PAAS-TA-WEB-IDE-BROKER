package org.paasta.servicebroker.webide.model;


import java.util.Map;

public class DeploymentInstance {

    public DeploymentInstance(Map map) {
        this.id = nullCheck(map.get("id"));
        this.vmCid = nullCheck(map.get("vm_cid"));
        this.diskCid = nullCheck(map.get("disk_cid"));
        this.agentId = nullCheck(map.get("agent_id"));
        this.ips = nullCheck(map.get("ips"));
        this.jobName = nullCheck(map.get("job_name"));
        this.jobState = nullCheck(map.get("job_state"));
        this.state = nullCheck(map.get("state"));
        this.active = nullCheck(map.get("active"));
    }

    public DeploymentInstance() {
    }

    private String nullCheck(Object object) {
        return object == null ? null : object.toString();
    }

    private String id;

    private String jobName;

    private String state;

    private String ips;

    private String vmCid;

    private String agentId;

    private String jobState;

    private String diskCid;

    private String active;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getIps() {
        return ips;
    }

    public void setIps(String ips) {
        this.ips = ips;
    }

    public String getVmCid() {
        return vmCid;
    }

    public void setVmCid(String vmCid) {
        this.vmCid = vmCid;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getJobState() {
        return jobState;
    }

    public void setJobState(String jobState) {
        this.jobState = jobState;
    }

    public String getDiskCid() {
        return diskCid;
    }

    public void setDiskCid(String diskCid) {
        this.diskCid = diskCid;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "JpaDeploymentInstance{" +
                "id='" + id + '\'' +
                ", job_name='" + jobName + '\'' +
                ", state='" + state + '\'' +
                ", ips='" + ips + '\'' +
                ", vmCid='" + vmCid + '\'' +
                ", agentId='" + agentId + '\'' +
                ", jobState='" + jobState + '\'' +
                ", diskCid='" + diskCid + '\'' +
                ", active='" + active + '\'' +
                '}';
    }
}

