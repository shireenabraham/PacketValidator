package com.illumio.firewall.ruleengine.impl;

import static com.illumio.firewall.model.NetworkPacket.PacketDirection;
import static com.illumio.firewall.model.NetworkPacket.Protocol;
import com.illumio.firewall.model.Range;
import com.illumio.firewall.ruleengine.RuleEngine;
import com.illumio.firewall.model.NetworkPacket;

import java.util.HashSet;
import java.util.TreeSet;
import java.util.List;

public class RuleEngineImpl implements RuleEngine {
	private HashSet<PacketDirection> allowedDirections = new HashSet<>();
	private HashSet<Protocol> allowedProtocols = new HashSet<>();
	
	/** 
	* Decision to choose between HashMap and TreeSet.
	* HashMap -> Occupies more space but Time complexity is O(1).
	* TreeSet -> Much efficient on Space but time complexity is O(logn)
	* TreeSet is chosen as we might we encountering large sets of data, but not too much to make logn time complexity hinder performance.
	*/
	private TreeSet<Range<Integer>> allowedPorts = new TreeSet<>();
	private TreeSet<Range<Double>> allowedIps = new TreeSet<>();

	public void updateRules(List<NetworkPacket> networkPacketRules) {
		networkPacketRules.forEach(rule -> {
			allowedDirections.add(rule.getDirection());
			allowedProtocols.add(rule.getProtocol());
			
			Range<Integer> ports = rule.getPortRange();
			if (allowedPorts.contains(ports)){
				Range<Integer> removePorts = allowedPorts.ceiling(ports);

				ports = ports.merge(removePorts);
				allowedPorts.remove(removePorts);
			}
			allowedPorts.add(ports);
			
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
		return allowedDirections.contains(packet.getDirection())
				&& allowedProtocols.contains(packet.getProtocol())
				&& allowedPorts.contains(packet.getPortRange())
				&& allowedIps.contains(packet.getIpAddressRange());
	}
}