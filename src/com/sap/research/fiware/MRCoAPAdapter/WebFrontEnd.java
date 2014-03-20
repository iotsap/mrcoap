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

package com.sap.research.fiware.MRCoAPAdapter;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sap.research.fiware.MRCoAPAdapter.datastructures.Configuration;
import com.sap.research.fiware.MRCoAPAdapter.datastructures.MoteRunnerInstance;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Scanner;

public class WebFrontEnd {

    public WebFrontEnd() {
        HttpServer server = null;
        try {
            server = HttpServer.create(new InetSocketAddress(Integer.parseInt(Configuration.getWebserverport())), 0);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        server.createContext("/fiMRCoAPAdapter", new MyHandler());
        server.createContext("/sap.css", new MyResourceHandler());

        server.setExecutor(null); // creates a default executor
        server.start();
    }

    static class MyResourceHandler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
           String response = " body {\n" +
                   "    color: purple;\n" +
                   "    background-color: #d8da3d }";
        InputStream in = getClass().getResourceAsStream("/sap.css");
        response = new Scanner(in,"UTF-8").useDelimiter("\\A").next();

           t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());


            os.write(response.getBytes());

            os.close();
        }
    }

    static class MyHandler implements HttpHandler {
        public void handleMain(HttpExchange t) throws IOException {

            String response = "<html><header>";
            response += "<link rel=\"stylesheet\" type=\"text/css\" href=\"/sap.css\">";
            response += "</header><body><h1>FI-WARE Moterunner CoAP Adapter</h1>";
            response += "<p>Moterunner Status: ";
            if (fwCoAPMRAdapter.mr.connected)
                response += " connected";
            else
                response += " not connected";

            // List nodes

            response += "</p><h1>Nodes</h1>";
            response += "<p>The following motes are connected to the MRCoAP Adapter and are exposed to NGSI.</p><table>";

            for (MoteRunnerInstance instance : fwCoAPMRAdapter.mr.instances) {
                response += "<tr><td>"+instance.getName()+"</td><td><a href=\"/fiMRCoAPAdapter/"+instance.getIpv6Short()+"\">Detail View</a></td></tr>";
            }

            // com.sap.research.fiware.MRCoAPAdapter.NGSI nodes

            response += "</table><h1>NGSI Endpoints</h1><p>The following NGSI endpoints have been configured at startup. Please refer to the documentation in case you need to alter these settings.</p><table>";
            response += "<tr><td>NGSI 9</td><td>"+Configuration.getNgsi9Server()+"</td></tr>";
            response += "<tr><td>NGSI 10</td><td>"+Configuration.getNgsi10Server()+"</td></tr>";


            response += "</table><p><a href=\"javascript:location.reload()\">Reload page</a></p>";

            response += "<hr> This Software comes without any warrenty.For usage, support and licensing information please refer to the FI-WARE catalog.";

            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());

            os.write(response.getBytes());
            os.close();
        }

        public void handleDetail(HttpExchange t) throws IOException {
            String response = "<html><header>";
            response += "<link rel=\"stylesheet\" type=\"text/css\" href=\"/sap.css\">";
            response += "</header><body><h1>FI-WARE Moterunner CoAP Adapter - Detail</h1>";

            String detailNo= t.getRequestURI().toString();
            detailNo = detailNo.substring(17);
            detailNo = detailNo.replace("/","");
            response += "<p>Moterunner Status: ";
            if (fwCoAPMRAdapter.mr.connected)
                response += " connected";
            else
                response += " not connected";

            // List nodes

            response += "</p><h1>Node Detail</h1>";

            for (MoteRunnerInstance instance : fwCoAPMRAdapter.mr.instances) {
                if (instance.getIpv6Short().equals(detailNo)) {
                 response += "<table>";
                 response += "<tr><td>Name</td><td>"+instance.getName()+"</td></tr>";
                 response += "<tr><td>IP</td><td>"+instance.getIpv6Addr()+"</td></tr>";
                 response += "<tr><td>Battery Status</td><td>"+instance.getBattery()+"</td></tr>";
                 response += "<tr><td>Entity</td><td>"+instance.getEntity()+"</td></tr>";
                 response += "<tr><td>Value</td><td>"+instance.getValue()+"</td></tr>";
                 response += "</table>";
                }
            }
            response += "<p><a href=\"javascript:location.reload()\">Reload</a</p>";
            response += "<p><a href=\"/fiMRCoAPAdapter/\">Back</a</p>";

            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());


            os.write(response.getBytes());

            os.close();
        }

        public void handle(HttpExchange t) throws IOException {
            String uriString = t.getRequestURI().toString();

            if (uriString.length()>17) {
                handleDetail(t);
                return;
            }

            handleMain(t);
        }

    }
}
