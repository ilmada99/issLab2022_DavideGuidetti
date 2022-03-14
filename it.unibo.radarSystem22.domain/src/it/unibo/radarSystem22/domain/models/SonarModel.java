package it.unibo.radarSystem22.domain.models;

import it.unibo.radarSystem22.domain.Distance;
import it.unibo.radarSystem22.domain.concrete.SonarConcrete;
import it.unibo.radarSystem22.domain.interfaces.IDistance;
import it.unibo.radarSystem22.domain.interfaces.ISonar;
import it.unibo.radarSystem22.domain.mock.SonarMock;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;

public abstract class SonarModel implements ISonar {
	protected boolean state = false; //false: non attivo, true: attivo
	protected IDistance currentValue = new Distance (90); //valore iniziale
	
	// create effettuata in Mock o Concrete in base alle specifiche
	public static ISonar create() {
		ISonar sonar;
		if (DomainSystemConfig.simulation) //file json che definisce la scelta di esecuzione del sistema
			sonar = createSonarMock();
		else
			sonar = createSonarConcrete();
		return sonar;
	}

	public static ISonar createSonarMock() {
		return new SonarMock();
	}

	public static ISonar createSonarConcrete() {
		return new SonarConcrete();
	}
	
	protected abstract void sonarSetUp(); //setUp sonar
	
	protected abstract void sonarProduce(); //produce value sonar
	
	//protected setState essendo un sistema immodificabile
	protected void setState(boolean state) {
		this.state = state;
	}

	public boolean isActive() {
		return this.state;
	}

	public IDistance getDistance() {
		return this.currentValue;
	}

}
