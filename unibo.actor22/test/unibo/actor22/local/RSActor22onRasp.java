package unibo.actor22.local;

import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;
import unibo.actor22.Qak22Util;
import unibo.actor22.common.ApplData;
import unibo.actor22.common.ControllerForActor;
import unibo.actor22.common.ControllerForLedActor;
import unibo.actor22.common.LedActor;
import unibo.actor22.common.RadarSystemConfig;
import unibo.actor22.common.SonarActor;
import unibo.actor22comm.utils.ColorsOut;
import unibo.actor22comm.utils.CommSystemConfig;
import unibo.actor22comm.utils.CommUtils;

public class RSActor22onRasp {

	public void doJob() {
		ColorsOut.outappl("ControllerUsingSonarAndLedOnPc | Start", ColorsOut.BLUE);
		configure();
		CommUtils.aboutThreads("Before execute - ");
		execute();
		terminate();
	}

	protected void configure() {
		DomainSystemConfig.simulation = false;
		DomainSystemConfig.ledGui = false;
		DomainSystemConfig.tracing = true;
		CommSystemConfig.tracing = false;
		RadarSystemConfig.sonarObservable 	= false; 

		new LedActor(ApplData.ledName);
		new SonarActor(ApplData.sonarName);
		new ControllerForActor(ApplData.controllerName);

	}

	protected void execute() {
		Qak22Util.sendAMsg(ApplData.activateCrtl);
	}

	public void terminate() {
		CommUtils.aboutThreads("Before exit - ");
		CommUtils.delay(3000);
		System.exit(0);
	}

	public static void main(String[] args) {
		CommUtils.aboutThreads("Before start - ");
		new RSActor22onRasp().doJob();
		CommUtils.aboutThreads("Before end - ");
	}

}