package com.illumio.firewall;

import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import au.com.bytecode.opencsv.CSVReader;

import static com.illumio.firewall.model.NetworkPacket.PacketDirection;
import static com.illumio.firewall.model.NetworkPacket.Protocol;
import com.illumio.firewall.model.Range;
import com.illumio.firewall.model.NetworkPacket;
import com.illumio.firewall.ruleengine.RuleEngine;
import com.illumio.firewall.ruleengine.RuleEngineFactory;


public class Firewall {

	private static final String DEFAULT_RULE_ENGINE = "impl";

	private String ruleEngineImplementation;
	private CSVReader reader;
	private RuleEngine ruleEngine;

	private final RuleEngineFactory ruleEngineFactory = new RuleEngineFactory();
	
	/** 
	* Constructors method for initilizing the rule engine from rules file
	*/
	public Firewall(String rulesCSVFile) throws FileNotFoundException, IOException {
		initRuleEngine(rulesCSVFile, DEFAULT_RULE_ENGINE);
	}

	public Firewall(String rulesCSVFile, String ruleEngineImplementation) throws FileNotFoundException, IOException {
		initRuleEngine(rulesCSVFile, ruleEngineImplementation);
	}

	/**
	* Method to accept packet
	*/

	public boolean acceptPacket(String direction, String protocol, String port, String ipAddress) throws FileNotFoundException, IOException {
		return ruleEngine.evaluatePacket(constructNetworkPacket(direction, protocol, port, ipAddress));
	}

	private void initRuleEngine(String rulesCSVFile, String ruleEngineImplementation) throws FileNotFoundException, IOException {
		List<NetworkPacket> networkPackets = new ArrayList<>();

		reader = new CSVReader(new FileReader(rulesCSVFile), ',');
		String[] row;
      	while ((row = reader.readNext()) != null) {
			if (row != null) {
				networkPackets.add(constructNetworkPacket(row[0], row[1], row[2], row[3]));
			}
       	}

       	ruleEngine = ruleEngineFactory.getRuleEngine(ruleEngineImplementation);
       	ruleEngine.updateRules(networkPackets);
	}

	private NetworkPacket constructNetworkPacket(String directionString, String protocolString, String port, String ipAddress) throws FileNotFoundException, IOException {
		PacketDirection direction = PacketDirection.getEnum(directionString);
        Protocol protocol = Protocol.getEnum(protocolString);

        /** Parsing ports */
        String[] portsString = port.split("-");
        portsString[0] = portsString[0].trim();
        Range<Integer> portRange = new Range<Integer>(Integer.parseInt(portsString[0]), Integer.parseInt(portsString[0]));
        if (portsString.length > 1){
        	portRange = new Range<Integer>(Integer.parseInt(portsString[0]), Integer.parseInt(portsString[1].trim()));
        }

        /** Parsing IP */
        String[] ipAddressString = ipAddress.split("-");
        Double fromIpAddress = convertIpToDouble(ipAddressString[0].trim());
        Range<Double> ipAddressRange = new Range<Double>(fromIpAddress, fromIpAddress);
        if (ipAddressString.length > 1){
        	ipAddressRange = new Range<Double>(fromIpAddress, convertIpToDouble(ipAddressString[1].trim()));
        }

        /** Create network packet */
        return new NetworkPacket(direction, protocol, portRange, ipAddressRange);
	}

	private Double convertIpToDouble(String ipAddressString) {
		String[] ipAddressStringParts = ipAddressString.split("\\.");

		return new Double(16777216 * Double.parseDouble(ipAddressStringParts[0]) 
								+ 65536 * Double.parseDouble(ipAddressStringParts[1]) 
								+ 256 * Double.parseDouble(ipAddressStringParts[2]) 
								+ Double.parseDouble(ipAddressStringParts[3]));
	}
}