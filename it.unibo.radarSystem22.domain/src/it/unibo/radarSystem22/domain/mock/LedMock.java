package it.unibo.radarSystem22.domain.mock;

import it.unibo.radarSystem22.domain.interfaces.ILed;
import it.unibo.radarSystem22.domain.models.LedModel;

public class LedMock extends LedModel implements ILed{

	@Override
	protected void ledActivate(boolean val) {		
		System.out.println("MOCK LED: "+val);
	}

}
