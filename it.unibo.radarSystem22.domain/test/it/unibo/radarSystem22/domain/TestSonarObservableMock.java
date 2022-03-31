package it.unibo.radarSystem22.domain;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import it.unibo.radarSystem22.domain.interfaces.ISonar;
import it.unibo.radarSystem22.domain.interfaces.ISonarObserver;
import it.unibo.radarSystem22.domain.models.SonarObservable;
import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;

public class TestSonarObservableMock {
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
			System.out.println("TestSonarObservableMockOn");
			//DomainSystemConfig.simulation = true;
		    ISonar sonar = DeviceFactory.createObservableSonar();
		    ISonarObserver observer1 = new SonarObserver("0");
			ISonarObserver observer2 = new SonarObserver("1");
			((SonarObservable)sonar).addObserver(observer1);
			((SonarObservable)sonar).addObserver(observer2);
			
		    sonar.activate();
			assertTrue(sonar.isActive());
			int v0=sonar.getDistance().getVal();
			int sonarDistance=sonar.getDistance().getVal();
			
			assertEquals(90,sonarDistance);
			assertEquals(90,observer1.getVal());
			assertEquals(90,observer2.getVal());
			while(observer1.getVal()>0 && observer2.getVal()>0) {
				sonarDistance = observer1.getVal();
				//System.out.println("sonar distance: "+sonarDistance);
				int vExceptedMin=v0-INTERVAL;
				int vExceptedMax=v0+INTERVAL;
				assertTrue(sonarDistance<=vExceptedMax && sonarDistance>=vExceptedMin);
				assertTrue(observer1.getVal()<=vExceptedMax && observer1.getVal()>=vExceptedMin);
				assertTrue(observer2.getVal()<=vExceptedMax && observer2.getVal()>=vExceptedMin);
				assertTrue(observer2.getVal()==observer1.getVal()); //check same value observer
				v0=sonarDistance;
				BasicUtils.delay(DomainSystemConfig.sonarDelay/2); //sonar delay
			}
			assertTrue(observer1.getVal()<=0 && observer2.getVal()<=0);
		}
		
		@Test
		public void testSonarMockOff() {
			System.out.println("TestSonarMockOff");
			//DomainSystemConfig.simulation = true;
			ISonar sonar = DeviceFactory.createObservableSonar();
		    sonar.activate();
		    sonar.deactivate();
			assertTrue(!sonar.isActive());
		}

	}
