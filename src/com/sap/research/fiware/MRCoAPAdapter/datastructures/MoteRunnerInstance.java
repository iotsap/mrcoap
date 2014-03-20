/*
FiWARE Moterunner CoAP Adapter
SAP AG
Modified BSD License
 ====================

Copyright (c) 2012, SAP AG
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
    * Neither the name of the SAP AG nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL SAP AG BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
