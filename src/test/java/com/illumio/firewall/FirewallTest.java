package com.illumio.firewall;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.lang.AssertionError;

import com.illumio.firewall.config.FirewallTestConfigs;
import com.illumio.firewall.config.FirewallTestConfig;

import com.illumio.firewall.Firewall;

public class FirewallTest {

	public static final String TEST_CONFIG_METADATA = "src/test/test-dependencies/test-configs.json";
	Throwable errors = new Throwable();

	@Test
	public void testPacketFirewall() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		FirewallTestConfigs firewallTestConfigs = objectMapper.readValue(new File(TEST_CONFIG_METADATA), FirewallTestConfigs.class);

		firewallTestConfigs.getFirewallTestConfigs().forEach(this::runCurrentTest);
	}

	private void runCurrentTest(FirewallTestConfig firewallTestConfig) {
		try{
			
			//Firewall firewall = new Firewall(firewallTestConfig.getRulesCsv(), firewallTestConfig.getRuleEngine());

		} catch (AssertionError | Exception e) {
			errors.addSuppressed(e);
		}
	}
}