/*
 FiWARE Moterunner CoAP Adapter
 SAP AG

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
