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
