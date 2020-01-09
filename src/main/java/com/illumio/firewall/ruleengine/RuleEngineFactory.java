package com.illumio.firewall.ruleengine;

import com.illumio.firewall.ruleengine.impl.RuleEngineImpl;

public class RuleEngineFactory {
	
	RuleEngine ruleEngine;

	public RuleEngine getRuleEngine(String type) {
		if (type.equalsIgnoreCase("impl")){
			ruleEngine = new RuleEngineImpl();
		}

		return ruleEngine;
	}
}