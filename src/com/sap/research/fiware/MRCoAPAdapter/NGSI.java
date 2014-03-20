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

import com.sap.research.fiware.MRCoAPAdapter.datastructures.Configuration;
import com.sap.research.fiware.MRCoAPAdapter.datastructures.MoteRunnerInstance;
import org.apache.http.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultBHttpClientConnection;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.protocol.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;


// todo: Check content type

public final class NGSI {
    synchronized private static final void HTTPPost(String inHost, int inPort, String Path, String xml) {
        HttpProcessor httpproc = HttpProcessorBuilder.create()
                .add(new RequestContent())
                .add(new RequestTargetHost())
                .add(new RequestConnControl())
                .add(new RequestUserAgent("MRCoAPAdapter/1.1"))
                .add(new RequestExpectContinue(true)).build();

        HttpRequestExecutor httpexecutor = new HttpRequestExecutor();

        if (Configuration.getVerbose())
          System.out.println("posting to host "+inHost+" // port: "+inPort+" // path: "+Path);

        HttpEntity requestBody = new StringEntity(xml, ContentType.create("text/xml", Consts.UTF_8));
        HttpCoreContext coreContext = HttpCoreContext.create();
        HttpHost host = new HttpHost(inHost, inPort);
        coreContext.setTargetHost(host);

        DefaultBHttpClientConnection conn = new DefaultBHttpClientConnection(8 * 1024);
        ConnectionReuseStrategy connStrategy = DefaultConnectionReuseStrategy.INSTANCE;
        try {
            if (!conn.isOpen()) {
                Socket socket = null;
                socket = new Socket(host.getHostName(), host.getPort());
                conn.bind(socket);
            }
            BasicHttpEntityEnclosingRequest request = new BasicHttpEntityEnclosingRequest("POST",
                    Path);
            request.setEntity(requestBody);
            httpexecutor.preProcess(request, httpproc, coreContext);
            HttpResponse response = httpexecutor.execute(request, conn, coreContext);
            httpexecutor.postProcess(response, httpproc, coreContext);

            if (Configuration.getVerbose())
                System.out.println("HTTP Response status: "+response.getStatusLine().toString());

            if (!connStrategy.keepAlive(response, coreContext)) {
                conn.close();
            }

        } catch (Exception e) {
            return;
        } finally {
            try {
                conn.close();
            } catch (IOException e) {
            }
        }
    }

    private static final String replaceXMLStrings(final MoteRunnerInstance id, String xml) {
        StringBuffer sb = new StringBuffer(xml);

        int idx = -1;
        while ((idx = sb.lastIndexOf("###entityID###")) != -1) {
            sb.replace(idx, idx + 14, id.getEntity());
        }

        idx = -1;
        while ((idx = sb.lastIndexOf("###value###")) != -1) {
            sb.replace(idx, idx + 11, id.getValue());
        }

        return sb.toString();
    }

    public static final void sendRegisterContext(final MoteRunnerInstance id) {
        // Read from resource
        String xml = null;
        InputStream in = fwCoAPMRAdapter.class.getResourceAsStream("/ngsi9.xml");
        xml = new Scanner(in,"UTF-8").useDelimiter("\\A").next();
        xml = replaceXMLStrings(id, xml);

        // Send
        URI uri = null;
        try {
            uri = new URI(Configuration.getNgsi9Server());
        } catch (URISyntaxException e) {
            System.err.println("NGSI9 URI malformed!.");
            System.exit(1);
        }

        String host = uri.getHost();
        int port = uri.getPort();
        String path = uri.getPath();

        HTTPPost(host,port,path, xml);
    }


    public static final void sendUpdateContext(final MoteRunnerInstance id) {
        // Read from resource
        String xml = null;
        InputStream in = fwCoAPMRAdapter.class.getResourceAsStream("/ngsi10.xml");
        xml = new Scanner(in,"UTF-8").useDelimiter("\\A").next();
        xml = replaceXMLStrings(id, xml);
        // Send
        URI uri = null;
        try {
            uri = new URI(Configuration.getNgsi10Server());
        } catch (URISyntaxException e) {
            System.err.println("NGSI10 URI malformed!.");
            System.exit(1);
        }

        String host = uri.getHost();
        int port = uri.getPort();
        String path = uri.getPath();

        HTTPPost(host,port,path, xml);
    }
}
