package unibo.actor22.events;

import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;
import unibo.actor22.Qak22Context;
import unibo.actor22.Qak22Util;
import unibo.actor22.common.ApplData;
import unibo.actor22.common.RadarSystemConfig;
import unibo.actor22comm.utils.ColorsOut;
import unibo.actor22comm.utils.CommSystemConfig;
import unibo.actor22comm.utils.CommUtils;

public class RSActor22DistSonarEventEmitter {
	public void doJob() {
		configure();
		CommUtils.aboutThreads("Before execute - ");
		execute();
	}
	
	protected void configure() {
		DomainSystemConfig.simulation = true;
		DomainSystemConfig.tracing = false;
		DomainSystemConfig.sonarDelay = 200;
		CommSystemConfig.tracing = false;

		// con false, il ControllerForSonarActor chiede la distanza,
		// con true, il ControllerForSonarActor agisce come observer
		RadarSystemConfig.sonarObservable = false;

		Qak22Context.handleLocalActorDecl(this);
		if (RadarSystemConfig.sonarObservable) {
			Qak22Context.registerAsEventObserver(ApplData.controllerName, ApplData.evDistance);
		}
	}

	protected void execute() {
		ColorsOut.outappl("UsingActorsWithAnnotOnPc | execute", ColorsOut.MAGENTA);
		Qak22Util.sendAMsg(ApplData.activateCrtl);
//		CommUtils.delay(3000);
//		Qak22Util.sendAMsg( ApplData.deactivateSonar );
	}

	public static void main(String[] args) {
		CommUtils.aboutThreads("Before start - ");
		new TestSonarActor22().doJob();
		CommUtils.aboutThreads("Before end - ");
	}
}
