/*
 FiWARE Moterunner CoAP Adapter
 SAP AG

*/

package com.sap.research.fiware.MRCoAPAdapter;

import com.sap.research.fiware.MRCoAPAdapter.datastructures.Configuration;
import org.apache.commons.cli.*;


public class fwCoAPMRAdapter {
    public static WebFrontEnd wf;
    public static MoteRunner mr;
    public static String version = "V2014R01";

    private static void printIntro() {
        System.out.println("MRCoAP Adapter");
        System.out.println("Version "+version);
    }

    private static void processArgs(String[] args) {
        Options options = new Options();
        Option help = new Option( "help", false, "print this message" );
        Option verbose = new Option( "verbose", "be extra verbose" );
        Option ngsi9 = new Option( "ngsi9", true, "specify ngsi9 endpoint");
        Option ngsi10 = new Option( "ngsi10", true, "specify ngsi10 endpoint");
        Option mrpaddr = new Option( "mrnetwork", true, "moterunner network prefix");
        Option mrgatewaynode = new Option( "mrgateway", true, "moterunner gateway edge node address");
        Option port = new Option( "port", true, "user interface http port");


        options.addOption(help).addOption(verbose).addOption(ngsi9).addOption(ngsi10).addOption(mrpaddr).addOption(mrgatewaynode).addOption(port);

        CommandLineParser parser = new BasicParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse( options, args);
        } catch (ParseException e) {
            System.err.println("Could not parse command line");
            System.exit(1);
        }

        if (cmd.hasOption("help")) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("fwcoapmradapter", options, true);
            System.exit(0);
        }

        if (cmd.hasOption("ngsi9"))
            Configuration.setNgsi9Server(cmd.getOptionValue("ngsi9"));
        if (cmd.hasOption("ngsi10"))
            Configuration.setNgsi10Server(cmd.getOptionValue("ngsi10"));
        if (cmd.hasOption("mrnetwork"))
            Configuration.setMoterunnerNetworkPrefix(cmd.getOptionValue("mrnetwork"));
        if (cmd.hasOption("mrgateway"))
            Configuration.setEdgeNode(cmd.getOptionValue("mrgateway"));
        if (cmd.hasOption("port"))
            Configuration.setWebserverport(cmd.getOptionValue("port"));
        if (cmd.hasOption("verbose"))
            Configuration.setVerbose(true);
    }

    public static void main(String[] args) {
        printIntro();
        processArgs(args);

        System.out.println("Starting Web-Frontend");
        wf = new WebFrontEnd();

        System.out.println("Web-Frontend started");

        mr = new MoteRunner();
        mr.start();
        System.out.println("The FIWARE MRCoAP adapter is now up and running.");
    }

}
