package it.unibo.radarSystem22.domain;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import it.unibo.radarSystem22.domain.interfaces.ISonar;
import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;

public class TestSonarMock {

	@Before
	public void up() {
		System.out.println("up");
	}
	
	@After
	public void down() {
		System.out.println("down");
	}
	
	@Test
	public void testSonarMockOn() {
		System.out.println("TestSonarMockOn");
		System.out.println("simulation: "+DomainSystemConfig.simulation);
		//DomainSystemConfig.simulation = true;
	    ISonar sonar = DeviceFactory.createSonar();
	    sonar.activate();
		assertTrue(sonar.isActive());
		int sonarDistance=sonar.getDistance().getVal();
		assertEquals(90,sonarDistance);
		while(sonarDistance>=DomainSystemConfig.DLIMIT) {
			sonarDistance = sonar.getDistance().getVal();
			System.out.println("sonar distance: "+sonarDistance);
			BasicUtils.delay(250);
		}
		assertTrue(sonarDistance<DomainSystemConfig.DLIMIT);
	}
	
	@Test
	public void testSonarMockOff() {
		System.out.println("TestSonarMockOff");
		System.out.println("simulation: "+DomainSystemConfig.simulation);
		//DomainSystemConfig.simulation = true;
	    ISonar sonar = DeviceFactory.createSonar();
	    sonar.activate();
	    sonar.deactivate();
		assertTrue(!sonar.isActive());
	}

}
