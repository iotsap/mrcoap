/*
 FiWARE Moterunner CoAP Adapter
 SAP AG

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
