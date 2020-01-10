package com.illumio.firewall;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.AssertionError;

import au.com.bytecode.opencsv.CSVReader;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Assert;
import org.junit.Test;

import com.illumio.firewall.config.FirewallTestConfigs;
import com.illumio.firewall.config.FirewallTestConfig;

import com.illumio.firewall.Firewall;

public class FirewallTest {

	final String TEST_CONFIG_METADATA = "src/test/test-dependencies/test-configs.json";
	final ObjectMapper objectMapper = new ObjectMapper();
	
	Throwable errors = new Throwable();
	CSVReader reader;

	@Test
	public void testPacketFirewall() throws Exception {
		FirewallTestConfigs firewallTestConfigs = objectMapper.readValue(new File(TEST_CONFIG_METADATA), FirewallTestConfigs.class);
		firewallTestConfigs.getFirewallTestConfigs().forEach(this::runCurrentTest);

		/* Check if all tests passed */
		if (errors.getSuppressed().length > 0){
			throw new AssertionError("Some tests have failed.", errors);
		}
	}

	/*
	* Running each test. We do not throw assertion errors or excpetion, rather we suppress and catch them so that all other tests are executed.
	* The suppressed exceptions are collected and thrown later to show a collective test result.
	*/
	private void runCurrentTest(FirewallTestConfig firewallTestConfig) {
		try{
			/* Initialize Firewall */
			Firewall firewall = new Firewall(firewallTestConfig.getRulesCsv(), firewallTestConfig.getRuleEngine());
			
			/* Read test data file */
			reader = new CSVReader(new FileReader(firewallTestConfig.getTestDataFile()), ',');
			
			/* Check acceptance of each packet and verify whether firewall acts as expected */
			String[] row;
			while ((row = reader.readNext()) != null) {	
				Boolean expectedResult = Boolean.parseBoolean(row[4]);
				Boolean actualResult = firewall.acceptPacket(row[0], row[1], row[2], row[3]);

				Assert.assertEquals("test "+firewallTestConfig.getTestNo(), expectedResult, actualResult);
			}

		} catch (AssertionError | Exception e) {
			errors.addSuppressed(e);
		}
	}
}