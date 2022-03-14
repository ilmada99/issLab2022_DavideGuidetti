package it.unibo.radarSystem22.domain.mock;

import it.unibo.radarSystem22.domain.Distance;
import it.unibo.radarSystem22.domain.interfaces.ISonar;
import it.unibo.radarSystem22.domain.models.SonarModel;
import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;

public class SonarMock extends SonarModel implements ISonar {
	private int value;
	private final int INTERVAL=1; //diminuzione progressiva del sonar
	
	@Override
	protected void sonarSetUp() {
		currentValue=new Distance(90);
		value=currentValue.getVal();
	}

	@Override
	protected void sonarProduce() {
		value = currentValue.getVal()-INTERVAL;
		currentValue=new Distance(value);
		this.setState(value >= DomainSystemConfig.DLIMIT); 
		//System.out.println("state: "+DomainSystemConfig.DLIMIT);
		BasicUtils.delay(DomainSystemConfig.sonarDelay); //delay tra una rilezione ed una altra
	}
	
	public void activate() {
		this.setState(true);
		System.out.println("SonarMock\nSTATE: "+state);
		//utilizzo thread per rilevazione sonar
		new Thread() {
			public void run() {
				while (state) {
					//inizio rilevazione sonar
					sonarProduce();
				}
				//tempo tra una rilevazione ed un'altra
				BasicUtils.delay(250);
			}
		}.start();
	}

	public void deactivate() {
		this.setState(false);
		System.out.println("STATE: "+getValue());
	}

	private int getValue(){
		return this.value;
	}
}
