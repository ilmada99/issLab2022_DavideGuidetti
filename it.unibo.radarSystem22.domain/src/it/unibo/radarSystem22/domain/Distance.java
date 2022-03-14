package it.unibo.radarSystem22.domain;

import it.unibo.radarSystem22.domain.interfaces.IDistance;

public class Distance implements IDistance {
	private int value;

	//costruttore che identifica la distanza iniziale
	public Distance(int distance) {
		value = distance;
	}

	public int getVal() {
		return value;
	}

	public String toString() {
		return String.valueOf(value);
	}
}