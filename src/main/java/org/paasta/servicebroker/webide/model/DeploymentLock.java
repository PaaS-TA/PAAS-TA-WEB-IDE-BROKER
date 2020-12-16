package org.paasta.servicebroker.webide.model;

import com.google.gson.annotations.SerializedName;

public class DeploymentLock {
    @SerializedName("resource")
    public String[] resuource;

    @SerializedName("task_id")
    public String task_id;

    @SerializedName("type")
    public String type;

    @SerializedName("timeout")
    public String timeout;
}
