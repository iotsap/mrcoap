/*
 FiWARE Moterunner CoAP Adapter
 SAP AG

*/

package com.sap.research.fiware.MRCoAPAdapter;

import ch.ethz.inf.vs.californium.CaliforniumLogger;
import ch.ethz.inf.vs.californium.coap.CoAP;
import ch.ethz.inf.vs.californium.coap.Request;
import ch.ethz.inf.vs.californium.coap.Response;

import com.sap.research.fiware.MRCoAPAdapter.datastructures.Configuration;
import com.sap.research.fiware.MRCoAPAdapter.datastructures.MoteRunnerInstance;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import com.ibm.bluez.v6lowpan.Connection;
import com.ibm.bluez.v6lowpan.EventHandler;
import com.ibm.bluez.v6lowpan.Node;

public class MoteRunner extends Thread implements EventHandler {
    public static ConcurrentLinkedQueue<MoteRunnerInstance> instances = new ConcurrentLinkedQueue<MoteRunnerInstance>();
    public static ConcurrentHashMap<MoteRunnerInstance, CommunicationHandler> handlerMap = new ConcurrentHashMap<MoteRunnerInstance, CommunicationHandler>();
    protected static AtomicBoolean exitAll = new AtomicBoolean();
    public static boolean connected;

    public MoteRunner() {
        if (!Configuration.getVerbose())
            CaliforniumLogger.disableLogging();

    }

    public void run() {
        int retry = 100;

        while (retry > 0) {
            try {
                Connection c = new Connection(Configuration.getMoterunnerNetworkPrefix(), Configuration.getEdgeNode(), this);
                while (true) {
                    Thread.sleep(100000);
                }
            } catch (Exception e) {
                System.out.println("<MR> Connect failed. Retry... "+(101-retry)+" out of 100");
            }
            retry--;
        }

        if (retry<0)
            System.exit(1);
    }

    /*
     Moterunner Events
     */

    public void onConnect(Connection conn) {
        System.out.println("<NR> Try to connect to edge mode.");
    }

    public void onDisconnect(Connection conn) {
        System.out.println("<MR> Fatal error: Edge Mote lost");
        this.exitAll.set(true);
        System.exit(0);
    }

    public void onNodeAdd(Node node) {
        connected = true;
        // Start a new connection handler for this node
        registerInstance(node);
    }

    public void onNodeDel(Node node) {
        removeInstance(node);
    }

    public void onNodeUpdate(Node node) {
    }


    public void registerInstance(Node node) {
        System.out.println("<MR> Register Instance");

        MoteRunnerInstance mri = new MoteRunnerInstance(node.getNodeIPAddr());
        mri.setName(node.getNodeIPAddr());
        mri.setIpv6Addr(node.getNodeIPAddr());
        mri.setIpv6Short(String.valueOf(node.getShortAddr()));

        this.instances.add(mri);

        try {
            CommunicationHandler ch = new CommunicationHandler(node.getConnection(), this, mri);
            handlerMap.put(mri, ch);
            ch.start();
        } catch (Exception e) {
        }
    }

    public void removeInstance(Node node) {
        System.out.println("<MR> Remove Instance");
        MoteRunnerInstance todel = null;
        for (MoteRunnerInstance instance : instances) {
            if (instance.getIpv6Addr().equals(node.getNodeIPAddr())) {
                todel = instance;
            }
        }

        if (instances.remove(todel) == true) {
            CommunicationHandler st = handlerMap.get(todel);
            if (st != null)
                st.exit.set(true);

            handlerMap.remove(todel);
        }

    }

}

class CommunicationHandler extends Thread {
    protected Connection conn;
    protected AtomicBoolean exit = new AtomicBoolean();
    protected MoteRunner moteRunner = null;
    protected MoteRunnerInstance instance = null;

    public CommunicationHandler(Connection conn, MoteRunner moteRunner, MoteRunnerInstance instance) {
        super();
        this.conn = conn;
        this.moteRunner = moteRunner;
        this.instance = instance;
    }

    private Request prepareGetObserveStatement() {
        Request request = Request.newGet();
        request.setObserve();
        request.setURI("coap://[" + instance.getIpv6Addr() + "]:1024/sensor/temp");
        return request;
    }

    private Request prepareGetStatement() {
        Request request = Request.newGet();
        request.setType(CoAP.Type.NON);
        request.setURI("coap://[" + instance.getIpv6Addr() + "]:1024/sensor/temp");
        return request;
    }

    private Request prepareGetStatus() {
        Request request = Request.newGet();
        request.setType(CoAP.Type.NON);
        request.setURI("coap://[" + instance.getIpv6Addr() + "]:1024/status");
        return request;
    }

    public void run() {
        // Create CoAP Request to send data via observe
        Request request = prepareGetObserveStatement();
        Request status  = prepareGetStatus();
        request.send();
        int round = 0;

        // wait for all incoming data till we have to quit
        do {
            Response response = null;
            try {
                response = request.waitForResponse(250000);
                if (response != null) {
                    int l = response.getPayloadSize();
                    byte[] ba = response.getPayload();

                    long a = 0;
                    if (l >= 4)
                        for (int i = 2; i <= 3; i++)
                            a = ((a << 8) | ba[i]);

                    for (int i = 0; i <= 1; i++)
                        a = ((a << 8) | ba[i]);

                    instance.setValue(String.valueOf((a * 0.01 - 40)));
                }
            } catch (InterruptedException e) {
                // just ignore
            }

            round++;

            if (round % 20==0) {
                round = 0;
                // get battery
                status.send();
                response = null;
                try {
                    response = status.waitForResponse(10000);
                    if (response != null) {
                        instance.setBattery(response.getPayloadString());
                    }
                } catch (InterruptedException e) {
                }

            }

            // Update value
        } while ((this.exit.get() == false) && (MoteRunner.exitAll.get() == false));

        // Say goodbye to mode (and don't care about response)
        Request requestCancel = prepareGetStatement();
        requestCancel.send();
    }
}

