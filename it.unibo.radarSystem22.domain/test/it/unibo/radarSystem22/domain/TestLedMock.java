package it.unibo.radarSystem22.domain;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import it.unibo.radarSystem22.domain.interfaces.ILed;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;

public class TestLedMock {
	
	@Before
	public void up() {
		System.out.println("up");
	}
	
	@After
	public void down() {
		System.out.println("down");
	}
	
	@Test
	public void testLedMockOn() {
		System.out.println("TestLedMockOn");
		System.out.println("simulation: "+DomainSystemConfig.simulation);
		//DomainSystemConfig.simulation = true;
	    ILed led = DeviceFactory.createLed();
		assertTrue(!led.getState());
		led.turnOn();
		assertTrue(led.getState());
	}
	
	@Test
	public void testLedMockOff() {
		System.out.println("TestLedMockOff");
		System.out.println("simulation: "+DomainSystemConfig.simulation);
		//DomainSystemConfig.simulation = true;
	    ILed led = DeviceFactory.createLed();
		assertTrue(!led.getState());
		led.turnOff();
		assertTrue(!led.getState());
	}

	/*
	@Test
	public void testLedConcrete() {
		
	}
	*/
}
