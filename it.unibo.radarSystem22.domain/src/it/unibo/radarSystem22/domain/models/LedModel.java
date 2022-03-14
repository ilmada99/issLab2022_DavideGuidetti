package it.unibo.radarSystem22.domain.models;

import it.unibo.radarSystem22.domain.concrete.LedConcrete;
import it.unibo.radarSystem22.domain.interfaces.ILed;
import it.unibo.radarSystem22.domain.mock.LedMock;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;

public abstract class LedModel implements ILed {
	protected boolean state = false; //stato accensione led

	//create effettuata in Mock o Concrete in base alle specifiche
	public static ILed create() {
		ILed led;
		if (DomainSystemConfig.simulation) //file json che definisce la scelta di esecuzione del sistema
			led = createLedMock();
		else
			led = createLedConcrete();
		return led;
	}

	public static ILed createLedMock() {
		return new LedMock();
	}

	public static ILed createLedConcrete() {
		return new LedConcrete();
	}

	//definito diversamente se mock o concrete
	protected abstract void ledActivate(boolean val);

	//protected setState essendo un sistema immodificabile
	protected void setState(boolean state) {
		this.state = state;
		ledActivate(state);
	}

	public void turnOn() {
		setState(true);
	}

	public void turnOff() {
		setState(false);
	}

	public boolean getState() {
		return this.state;
	}
}