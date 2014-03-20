/*
 FiWARE Moterunner CoAP Adapter
 SAP AG

*/

package com.sap.research.fiware.MRCoAPAdapter.datastructures;

import com.sap.research.fiware.MRCoAPAdapter.NGSI;

import java.util.Date;

public class MoteRunnerInstance {
    private String name;
    private String ipv6Addr;
    private String ipv6Short;
    private String entity;
    private String battery;
    private String value;

    private String oldValue;

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    private Date lastUpdate;

    public String getBattery() {
        return battery;
    }

    public void setBattery(String battery) {
        this.battery = battery;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
        if (!oldValue.equals(value)) {
            NGSI.sendUpdateContext(this);
        }
        this.oldValue = value;
    }

    public MoteRunnerInstance(String entity, String ipv6Short, String ipv6Addr, String name) {
        this.entity = entity;
        this.ipv6Short = ipv6Short;
        this.ipv6Addr = ipv6Addr;
        this.name = name;
        this.battery = "100";
        this.value = "not read";
        this.lastUpdate =  new Date();
        this.oldValue = this.value;
        NGSI.sendRegisterContext(this);
    }

    public MoteRunnerInstance(String entity) {
        this.entity = entity;
        this.ipv6Addr = "";
        this.ipv6Short = "";
        this.name = "default";
        this.battery = "100";
        this.value = "not read";
        this.oldValue = this.value;
        this.lastUpdate =  new Date();
        NGSI.sendRegisterContext(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIpv6Addr() {
        return ipv6Addr;
    }

    public void setIpv6Addr(String ipv6Addr) {
        this.ipv6Addr = ipv6Addr;
    }

    public String getIpv6Short() {
        return ipv6Short;
    }

    public void setIpv6Short(String ipv6Short) {
        this.ipv6Short = ipv6Short;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }
}
