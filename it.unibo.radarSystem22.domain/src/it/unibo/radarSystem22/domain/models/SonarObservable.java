package it.unibo.radarSystem22.domain.models;

import java.util.ArrayList;
import java.util.List;

import it.unibo.radarSystem22.domain.Distance;
import it.unibo.radarSystem22.domain.concrete.SonarObservableConcrete;
import it.unibo.radarSystem22.domain.interfaces.*;
import it.unibo.radarSystem22.domain.mock.SonarObservableMock;
import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.domain.utils.ColorsOut;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;

public abstract class SonarObservable implements ISonar {
	protected IDistance curVal = new Distance(90);
	protected boolean stopped = true;

	private List<ISonarObserver> observers = new ArrayList<>();
	
	public void addObserver(ISonarObserver observer) {
		observer.update(this.curVal);
		this.observers.add(observer);
	}

	public void removeObserver(ISonarObserver observer) {
		this.observers.remove(observer);
	}

	public static ISonar create() {
		if (DomainSystemConfig.simulation)
			return createSonarObservableMock();
		else
			return createSonarObservableConcrete();
	}

	public static ISonar createSonarObservableMock() {
		ColorsOut.out("createSonarObservableMock", ColorsOut.BLUE);
		return new SonarObservableMock();
	}

	public static ISonar createSonarObservableConcrete() {
		ColorsOut.out("createSonarObservableConcrete", ColorsOut.BLUE);
		return new SonarObservableConcrete();
	}

	protected SonarObservable() {
		ColorsOut.out("SonarModel | calling (specialized) sonarSetUp ", ColorsOut.BLUE);
		sonarSetUp();
	}

	protected void updateDistance(int d) {
		curVal = new Distance(d);
		ColorsOut.out("SonarModel | updateDistance " + d, ColorsOut.BLUE);
		for (ISonarObserver observer : this.observers) {
            observer.update(this.curVal);
        }
	}

	protected abstract void sonarSetUp();

	protected abstract void sonarProduce();

	@Override
	public boolean isActive() {
		// ColorsOut.out("SonarModel | isActive "+ (! stopped), ColorsOut.GREEN);
		return !stopped;
	}

	@Override
	public IDistance getDistance() {
		return curVal;
	}

	@Override
	public void activate() {
		curVal = new Distance(90);
		ColorsOut.out("SonarModel | activate");
		stopped = false;
		new Thread() {
			public void run() {
				while (!stopped) {
					ColorsOut.out("SonarModel | call produce", ColorsOut.GREEN);
					sonarProduce();
					BasicUtils.delay(DomainSystemConfig.sonarDelay);
				}
				ColorsOut.out("SonarModel | ENDS");
			}
		}.start();
	}

	@Override
	public void deactivate() {
		ColorsOut.out("SonarModel | deactivate", ColorsOut.BgYellow);
		stopped = true;
	}

}
