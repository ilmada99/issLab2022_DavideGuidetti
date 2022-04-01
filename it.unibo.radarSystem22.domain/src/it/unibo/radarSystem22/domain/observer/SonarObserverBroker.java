package it.unibo.radarSystem22.domain.observer;

import java.util.ArrayList;
import java.util.List;

import it.unibo.radarSystem22.domain.Distance;
import it.unibo.radarSystem22.domain.interfaces.IDistance;
import it.unibo.radarSystem22.domain.interfaces.ISonarObserver;

public class SonarObserverBroker {
	private IDistance curVal = new Distance(90);
	private List<ISonarObserver> observers = new ArrayList<>();
	
	public void addObserver(ISonarObserver observer) {
		observer.update(this.curVal);
		this.observers.add(observer);
	}

	public void removeObserver(ISonarObserver observer) {
		this.observers.remove(observer);
	}
	
	public void udpate(IDistance curVal) {
		this.curVal=curVal;
		for (ISonarObserver observer : this.observers) {
            observer.update(this.curVal);
        }
	}
	
}
