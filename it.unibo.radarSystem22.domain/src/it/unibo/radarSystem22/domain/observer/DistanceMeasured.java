package it.unibo.radarSystem22.domain.observer;

import java.util.ArrayList;
import java.util.List;

import it.unibo.radarSystem22.domain.Distance;
import it.unibo.radarSystem22.domain.interfaces.IDistance;
import it.unibo.radarSystem22.domain.interfaces.IDistanceMeasured;
import it.unibo.radarSystem22.domain.interfaces.ISonarObserver;
import it.unibo.radarSystem22.domain.utils.ColorsOut;

public class DistanceMeasured implements IDistanceMeasured {
	private List<ISonarObserver> observers = new ArrayList<>();
	protected IDistance curVal = new Distance(90);

	public void addObserver(ISonarObserver observer) {
		observer.update(this.curVal);
		this.observers.add(observer);
	}

	public void removeObserver(ISonarObserver observer) {
		this.observers.remove(observer);
	}


	@Override
	public void update(IDistance curVal) {
		this.curVal=curVal;
		for (ISonarObserver observer : this.observers) {
            observer.update(this.curVal);
        }
	}

	@Override
	public int getVal() {
		return this.curVal.getVal();
	}

}
