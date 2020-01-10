package com.illumio.firewall.ruleengine;

import com.illumio.firewall.ruleengine.impl.RuleEngineImpl;

public class RuleEngineFactory {

	public RuleEngine getRuleEngine(String type) {
		if (type.equalsIgnoreCase("impl")){
			return new RuleEngineImpl();
		}

		return null;
	}
}