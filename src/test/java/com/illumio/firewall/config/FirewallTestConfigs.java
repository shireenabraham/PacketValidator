package com.illumio.firewall.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;

@Getter
public class FirewallTestConfigs {
	
	@JsonProperty("configMetaData")
	List<FirewallTestConfig> firewallTestConfigs;
}