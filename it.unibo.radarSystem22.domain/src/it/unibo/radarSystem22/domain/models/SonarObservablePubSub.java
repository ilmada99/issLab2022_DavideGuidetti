package it.unibo.radarSystem22.domain.models;

import java.util.ArrayList;
import java.util.List;

import it.unibo.radarSystem22.domain.Distance;
import it.unibo.radarSystem22.domain.concrete.SonarConcrete;
import it.unibo.radarSystem22.domain.concrete.SonarObservableConcrete;
import it.unibo.radarSystem22.domain.concrete.SonarObservableConcretePubSub;
import it.unibo.radarSystem22.domain.interfaces.*;
import it.unibo.radarSystem22.domain.mock.SonarMock;
import it.unibo.radarSystem22.domain.mock.SonarObservableMock;
import it.unibo.radarSystem22.domain.mock.SonarObservableMockPubSub;
import it.unibo.radarSystem22.domain.observer.SonarObserverBroker;
import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.domain.utils.ColorsOut;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;

public abstract class SonarObservablePubSub implements ISonar {
	protected IDistance curVal = new Distance(90);
	protected boolean stopped = true;
	private SonarObserverBroker sob = new SonarObserverBroker();

	public static ISonar create() {
		if (DomainSystemConfig.simulation)
			return createSonarObservableMockPubSub();
		else
			return createSonarObservableConcretePubSub();
	}

	public static ISonar createSonarObservableMockPubSub() {
		ColorsOut.out("createSonarObservableMock", ColorsOut.BLUE);
		return new SonarObservableMockPubSub();
	}

	public static ISonar createSonarObservableConcretePubSub() {
		ColorsOut.out("createSonarObservableConcrete", ColorsOut.BLUE);
		return new SonarObservableConcretePubSub();
	}

	protected SonarObservablePubSub() {
		ColorsOut.out("SonarModel | calling (specialized) sonarSetUp ", ColorsOut.BLUE);
		sonarSetUp();
	}

	protected void updateDistance(int d) {
		curVal = new Distance(d);
		ColorsOut.out("SonarModel | updateDistance " + d, ColorsOut.BLUE);
		sob.udpate(curVal);
	}

	protected abstract void sonarSetUp();

	protected abstract void sonarProduce();
	
	public SonarObserverBroker getSonarObserverBroker() {
		return this.sob;
	}

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
