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

public class Configuration {
  private static String ngsi9Server = "htpp://localhost:80/post.php";
  private static String ngsi10Server = "http://localhost:80/post.php";
  private static String moterunnerNetworkPrefix = "fc00:db8:5::";
  private static String edgeNode = "0000:0000:0000:0001";
  private static String webserverport = "8001";
  private static boolean verbose = false;

    public static boolean isVerbose() {
        return verbose;
    }

    public static void setVerbose(boolean verbose) {
        Configuration.verbose = verbose;
    }

    public static String getNgsi9Server() {
        return ngsi9Server;
    }

    public static void setNgsi9Server(String ngsi9Server) {
        Configuration.ngsi9Server = ngsi9Server;
    }

    public static String getNgsi10Server() {
        return ngsi10Server;
    }

    public static void setNgsi10Server(String ngsi10Server) {
        Configuration.ngsi10Server = ngsi10Server;
    }

    public static String getMoterunnerNetworkPrefix() {
        return moterunnerNetworkPrefix;
    }

    public static void setMoterunnerNetworkPrefix(String moterunnerNetworkPrefix) {
        Configuration.moterunnerNetworkPrefix = moterunnerNetworkPrefix;
    }

    public static String getEdgeNode() {
        return edgeNode;
    }

    public static void setEdgeNode(String edgeNode) {
        Configuration.edgeNode = edgeNode;
    }

    public static String getWebserverport() {
        return webserverport;
    }

    public static void setWebserverport(String webserverport) {
        Configuration.webserverport = webserverport;
    }

    public static boolean getVerbose() {
        return verbose;
    }
}
