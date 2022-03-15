package it.unibo.radarSystem22.domain;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import it.unibo.radarSystem22.domain.interfaces.ISonar;
import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;

public class TestSonarMock {
	private final int INTERVAL = 1; // diminuzione progressiva del sonar
	
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
		int v0=sonar.getDistance().getVal();
		int sonarDistance=sonar.getDistance().getVal();
		assertEquals(90,sonarDistance);
		while(sonarDistance>0) {
			sonarDistance = sonar.getDistance().getVal();
			System.out.println("sonar distance: "+sonarDistance);
			int vExceptedMin=v0-INTERVAL;
			int vExceptedMax=v0+INTERVAL;
			assertTrue(sonarDistance<=vExceptedMax && sonarDistance>=vExceptedMin);
			v0=sonarDistance;
			BasicUtils.delay(DomainSystemConfig.sonarDelay/2); //sonar delay
		}
		assertTrue(sonarDistance<=0);
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
