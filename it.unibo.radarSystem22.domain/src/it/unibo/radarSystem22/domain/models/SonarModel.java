package it.unibo.radarSystem22.domain.models;

import it.unibo.radarSystem22.domain.Distance;
import it.unibo.radarSystem22.domain.concrete.SonarConcrete;
import it.unibo.radarSystem22.domain.interfaces.*;
import it.unibo.radarSystem22.domain.mock.SonarMock;
import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.domain.utils.ColorsOut;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;

public abstract class SonarModel implements ISonar {
	protected IDistance curVal = new Distance(90);
	protected boolean stopped = true;

	public static ISonar create() {
		if (DomainSystemConfig.simulation)
			return createSonarMock();
		else
			return createSonarConcrete();
	}

	public static ISonar createSonarMock() {
		ColorsOut.out("createSonarMock", ColorsOut.BLUE);
		return new SonarMock();
	}

	public static ISonar createSonarConcrete() {
		ColorsOut.out("createSonarConcrete", ColorsOut.BLUE);
		return new SonarConcrete();
	}

	protected SonarModel() {
		ColorsOut.out("SonarModel | calling (specialized) sonarSetUp ", ColorsOut.BLUE);
		sonarSetUp();
	}

	protected void updateDistance(int d) {
		curVal = new Distance(d);
		ColorsOut.out("SonarModel | updateDistance " + d, ColorsOut.BLUE);
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
