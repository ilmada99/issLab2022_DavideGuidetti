package it.unibo.radarSystem22.domain.concrete;

import java.io.IOException;

import it.unibo.radarSystem22.domain.interfaces.ILed;
import it.unibo.radarSystem22.domain.models.LedModel;

public class LedConcrete extends LedModel implements ILed {
	private Runtime rt = Runtime.getRuntime();

	protected void ledActivate(boolean val) {
		System.out.println("CONCRETE VAL: " + val);
		try {
			if (val)
				rt.exec("sudo bash led25GpioTurnOn.sh"); //esecuzione file sh in accordo con le specifiche
			else
				rt.exec("sudo bash led25GpioTurnOff.sh"); //esecuzione file sh in accordo con le specifiche
		} catch (IOException e) {
			System.out.println("IOException");
		}
	}

}
