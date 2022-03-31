package it.unibo.radarSystem22.domain;

import it.unibo.radarSystem22.domain.concrete.RadarDisplay;
import it.unibo.radarSystem22.domain.interfaces.*;
import it.unibo.radarSystem22.domain.models.LedModel;
import it.unibo.radarSystem22.domain.models.SonarModel;
import it.unibo.radarSystem22.domain.models.SonarObservable;
import it.unibo.radarSystem22.domain.models.SonarObservablePubSub;

public class DeviceFactory {
	public static ILed createLed() {
		return LedModel.create();
	}

	public static ISonar createSonar() {
		return SonarModel.create();
	}
	
	public static ISonar createObservableSonar() {
		return SonarObservable.create();
	}
	
	public static ISonar createObservablePubSubSonar() {
		return SonarObservablePubSub.create();
	}

	public static IRadarDisplay createRadarGui() {
		return RadarDisplay.getRadarDisplay();
	}
}
