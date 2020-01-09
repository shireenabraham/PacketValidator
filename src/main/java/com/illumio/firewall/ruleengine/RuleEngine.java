package com.illumio.firewall.ruleengine;

import com.illumio.firewall.model.NetworkPacket;
import java.util.List;

public interface RuleEngine {
	void updateRules(List<NetworkPacket> networkPackets);
	boolean evaluatePacket(NetworkPacket networkPacket);
}