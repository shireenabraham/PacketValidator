package com.illumio.firewall.ruleengine.impl;

import static com.illumio.firewall.model.NetworkPacket.PacketDirection;
import static com.illumio.firewall.model.NetworkPacket.Protocol;
import com.illumio.firewall.model.Range;
import com.illumio.firewall.ruleengine.RuleEngine;
import com.illumio.firewall.model.NetworkPacket;

import java.util.TreeMap;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.List;

public class RuleEngineImpl implements RuleEngine {
	private HashMap<PacketDirection, HashMap<Protocol, TreeMap<Range<Integer>, TreeSet<Range<Double>>>>> allowedPackets = new HashMap<>();

	public void updateRules(List<NetworkPacket> networkPacketRules) {
		networkPacketRules.forEach(rule -> {
			/*
			* Add direction
			*/
			allowedPackets.putIfAbsent(rule.getDirection(), new HashMap<>());
			
			/*
			* Add protocol
			*/
			HashMap<Protocol, TreeMap<Range<Integer>, TreeSet<Range<Double>>>> allowedProtocols = allowedPackets.get(rule.getDirection());
			allowedProtocols.putIfAbsent(rule.getProtocol(), new TreeMap<>());

			/*
			* Add the ports
			*/
			TreeSet<Range<Double>> allowedIps = new TreeSet<>();

			TreeMap<Range<Integer>, TreeSet<Range<Double>>> allowedPorts = allowedProtocols.get(rule.getProtocol());
			Range<Integer> ports = rule.getPortRange();

			if (allowedPorts.containsKey(ports)){
				Range<Integer> removePorts = allowedPorts.ceilingKey(ports);

				allowedIps = allowedPorts.remove(removePorts);
				ports = ports.merge(removePorts);
			}
			allowedPorts.put(ports, allowedIps);

			/*
			* Add the Ips
			*/
			Range<Double> ips = rule.getIpAddressRange();
			if (allowedIps.contains(ips)){
				Range<Double> removeIps = allowedIps.ceiling(ips);

				ips = ips.merge(removeIps);
				allowedIps.remove(removeIps);
			}
			allowedIps.add(ips);
		});
	}

	public boolean evaluatePacket(NetworkPacket packet){
		
		/*
		* Contains the direction
		*/
		if (!allowedPackets.containsKey(packet.getDirection())) {
			return false;
		}

		/*
		* Contains the protocol
		*/
		if (!allowedPackets.get(packet.getDirection()).containsKey(packet.getProtocol())) {
			return false;
		}

		/*
		* port in range?
		*/
		if (!allowedPackets.get(packet.getDirection()).get(packet.getProtocol()).containsKey(packet.getPortRange())) {
			return false;
		}

		/*
		* IP in range?
		*/
		if (!allowedPackets.get(packet.getDirection()).get(packet.getProtocol()).get(packet.getPortRange()).contains(packet.getIpAddressRange())) {
			return false;
		}

		return true;
	}
}