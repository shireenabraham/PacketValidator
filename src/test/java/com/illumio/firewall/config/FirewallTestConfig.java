package com.illumio.firewall.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class FirewallTestConfig {

	private static final String DEFAULT_RULE_ENGINE = "impl";

	@JsonProperty("rulesCsv")
	String rulesCsv;

	@JsonProperty(value = "ruleEngine", defaultValue = DEFAULT_RULE_ENGINE)
	String ruleEngine;

	@JsonProperty("results")
	String resultsJsonFile;
}