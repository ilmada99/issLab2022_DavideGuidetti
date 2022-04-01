package it.unibo.radarSystem22.domain.observer;

import it.unibo.radarSystem22.domain.interfaces.IDistance;
import it.unibo.radarSystem22.domain.interfaces.ISonarObserver;

public class SonarObserver implements ISonarObserver {
	private String id;
	private IDistance curVal;

	public SonarObserver(String id) {
		this.id = id;
	}

	@Override
	public void update(IDistance curVal) {
		this.curVal=curVal;
		System.out.println("distance id "+this.id+": "+this.curVal.getVal());
	}

	public int getVal() {
		return this.curVal.getVal();
	}
}
